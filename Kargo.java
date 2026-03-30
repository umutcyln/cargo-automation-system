public class Kargo {
    private String takipNo;
    private Musteri gonderici, alici;
    private KargoDurumu durum;
    private double agirlik;
    private Kurye kurye; // Degisken adi 'kurye'

    public Kargo(String takipNo, Musteri gonderici, Musteri alici, double agirlik) {
        this.takipNo = takipNo;
        this.gonderici = gonderici;
        this.alici = alici;
        this.agirlik = agirlik;
        this.durum = KargoDurumu.KABUL_EDILDI;
        this.kurye = null;
    }

    public double ucretHesapla() { return agirlik * 15.0; }

    // --- GETTER VE SETTERLAR (KargoSistemi bunlari cagiracak) ---
    public String getTakipNo() { return takipNo; }
    
    public KargoDurumu getDurum() { return durum; }
    public void setDurum(KargoDurumu durum) { this.durum = durum; }
    
    public Musteri getGonderici() { return gonderici; }
    public Musteri getAlici() { return alici; }
    
    public Kurye getKurye() { return kurye; }
    public void setKurye(Kurye kurye) { this.kurye = kurye; }
}