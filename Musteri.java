public class Musteri extends Kisi {
    private String adres;

    public Musteri(String ad, String soyad, String telefon, String adres) {
        super(ad, soyad, telefon);
        this.adres = adres;
    }

    public String getAdres() { return adres; }
}