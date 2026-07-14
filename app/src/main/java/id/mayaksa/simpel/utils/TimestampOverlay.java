package id.mayaksa.simpel.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Kelas utility untuk menggambar overlay teks pada foto:
 * - kotak info semi transparan di bagian bawah foto
 * - teks: judul, alamat, koordinat, tanggal/waktu (Full Width)
 */
public class TimestampOverlay {

    /** Load bitmap dari path file, dan koreksi rotasi berdasarkan data EXIF kamera. */
    public static Bitmap loadRotatedBitmap(String filePath) throws IOException {
        Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(filePath);
        ExifInterface exif = new ExifInterface(filePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * Menggambar overlay teks lengkap di atas foto.
     */
    public static Bitmap drawOverlay(Bitmap source, String title, String address,
                                     double lat, double lon, String dateTimeStr) {

        Bitmap mutable = source.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutable);

        int w = mutable.getWidth();
        int h = mutable.getHeight();

        // --- Ukuran skala relatif terhadap resolusi foto agar teks tidak terlalu kecil/besar ---
        float scale = w / 1080f;
        float padding = 32 * scale; // Margin kiri dan kanan untuk teks

        // --- Karena map dihapus, tinggi area teks sedikit disesuaikan ---
        float boxHeight = 220 * scale;
        float boxTop = h - boxHeight - (24 * scale);
        float boxLeft = 0;
        float boxRight = w;

        // --- Kotak latar belakang gradasi gelap agar teks putih tetap terbaca ---
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setShader(new LinearGradient(0, boxTop, 0, h,
                Color.argb(0, 0, 0, 0), Color.argb(190, 0, 0, 0), Shader.TileMode.CLAMP));
        canvas.drawRect(boxLeft, boxTop, boxRight, h, bgPaint);

        // --- Pengaturan Layout Teks (kini memenuhi lebar foto) ---
        float textLeft = padding;
        float textRight = w - padding;
        float textWidth = textRight - textLeft;

        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.WHITE);
        titlePaint.setFakeBoldText(true);
        titlePaint.setTextSize(30 * scale);

        Paint bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bodyPaint.setColor(Color.WHITE);
        bodyPaint.setTextSize(24 * scale);

        Paint coordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordPaint.setColor(Color.WHITE);
        coordPaint.setFakeBoldText(true);
        coordPaint.setTextSize(26 * scale);

        // Titik Y awal (dari atas gradien)
        float y = boxTop + (40 * scale);

        // 1. Judul (nama tempat)
        canvas.drawText(truncate(title, textWidth, titlePaint), textLeft, y, titlePaint);
        y += 40 * scale;

        // 2. Alamat, dipecah otomatis jadi beberapa baris
        java.util.List<String> addressLines = wrapText(address, bodyPaint, textWidth);
        int maxLines = 3;
        for (int i = 0; i < Math.min(addressLines.size(), maxLines); i++) {
            canvas.drawText(addressLines.get(i), textLeft, y, bodyPaint);
            y += 30 * scale;
        }

        y += 10 * scale; // Jarak ekstra antara alamat dan koordinat

        // 3. Koordinat
        String coordText = String.format(Locale.US, "%.4f, %.4f", lat, lon);
        canvas.drawText(coordText, textLeft, y, coordPaint);
        y += 32 * scale;

        // 4. Tanggal & waktu
        canvas.drawText(dateTimeStr, textLeft, y, bodyPaint);

        return mutable;
    }

    private static String truncate(String text, float maxWidth, Paint paint) {
        if (paint.measureText(text) <= maxWidth) return text;
        String ellipsis = "...";
        int end = text.length();
        while (end > 0 && paint.measureText(text.substring(0, end) + ellipsis) > maxWidth) {
            end--;
        }
        return text.substring(0, Math.max(end, 0)) + ellipsis;
    }

    private static java.util.List<String> wrapText(String text, Paint paint, float maxWidth) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (text == null) return lines;
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String candidate = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (paint.measureText(candidate) > maxWidth && currentLine.length() > 0) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine = new StringBuilder(candidate);
            }
        }
        if (currentLine.length() > 0) lines.add(currentLine.toString());
        return lines;
    }

    /** Menimpa file foto asli dengan hasil bitmap yang sudah diberi overlay. */
    public static void saveBitmapToFile(Bitmap bitmap, String path) throws IOException {
        try (FileOutputStream out = new FileOutputStream(path)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out);
        }
    }

    /** Menyimpan bitmap ke galeri publik (MediaStore). */
    public static Uri saveToGallery(Context context, Bitmap bitmap, String displayName) throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TimestampCamera");
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            File dir = new File(android.os.Environment.getExternalStoragePublicDirectory(
                    android.os.Environment.DIRECTORY_PICTURES), "TimestampCamera");
            if (!dir.exists()) dir.mkdirs();
            values.put(MediaStore.Images.Media.DATA, new File(dir, displayName + ".jpg").getAbsolutePath());
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Uri item = context.getContentResolver().insert(collection, values);
        if (item == null) throw new IOException("Gagal membuat entri MediaStore");

        try (OutputStream out = context.getContentResolver().openOutputStream(item)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            context.getContentResolver().update(item, values, null, null);
        }

        return item;
    }
}