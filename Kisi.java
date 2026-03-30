public abstract class Kisi {
    protected String ad, soyad, telefon;

    public Kisi(String ad, String soyad, String telefon) {
        this.ad = ad; 
        this.soyad = soyad; 
        this.telefon = telefon;
    }

    public String getTamAd() { return ad + " " + soyad; }
}