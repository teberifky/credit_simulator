# Credit Simulator

Aplikasi untuk menghitung **cicilan kredit kendaraan**.

## Prasyarat

- JDK 17+ dan Maven 3.8+

## Cara menjalankan (step by step)

```bash
git clone <url-repo> && cd credit_simulator

# 1. Tanpa file input
./credit_simulator            # atau: bin/credit_simulator

# 2. Dengan file input
./credit_simulator file_inputs.txt   # atau: bin/credit_simulator file_inputs.txt
```

Run pertama akan otomatis `mvn package` (jar tidak di-commit). Build manual: `mvn package`.

### Menu / command

Ketik `show` kapan saja untuk melihat semua command:

| Command         | Fungsi                                                       |
| --------------- | ------------------------------------------------------------ |
| `calculate`     | Hitung: jenis, kondisi, tahun, jumlah pinjaman, tenor, DP    |
| `save [nama]`   | Simpan hasil terakhir sebagai _sheet_ (default: sheet aktif) |
| `switch <nama>` | Pindah ke sheet tersimpan dan tampilkan hasilnya             |
| `sheets`        | Daftar sheet (`*` = aktif)                                   |
| `result`        | Tampilkan ulang hasil terakhir                               |
| `show`          | Tampilkan semua command                                      |
| `exit`          | Keluar                                                       |

Sheet disimpan di memori selama aplikasi berjalan (hilang setelah `exit`).

### Catatan: menu Load (web service) tidak diimplementasikan

Soal meminta menu _Load existing calculation_ yang memanggil web service JSON di
`https://run.mocky.io/v3/9108b1da-beec-409e-ae14-e8091955666c`. Saat dicoba, URL tersebut
mengembalikan **HTTP 404 Not Found** (bukan HTTP 200 seperti yang tertulis di soal), sehingga fitur
ini sengaja tidak diimplementasikan dan tidak ada command `load`. Karena itu aplikasi tidak memakai
jaringan maupun parser JSON sama sekali.

### Format file input

`file_inputs.txt` berisi command dan jawaban, satu per baris:

```
calculate
mobil
bekas
2020
100000000
3
25000000
save contoh1
exit
```

Jika file tidak diakhiri `exit`, aplikasi lanjut membaca dari keyboard.
