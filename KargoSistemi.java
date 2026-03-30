import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class KargoSistemi extends JFrame {
    
    private ArrayList<Kargo> cargoList = new ArrayList<>();
    private ArrayList<Kurye> kuryeHavuzu = new ArrayList<>();

    public KargoSistemi() {
        // --- 1. MODERN TEMA AYARI (Nimbus) ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
           
        }

        // --- KURYELER ---
        kuryeHavuzu.add(new Kurye("Ahmet", "Yilmaz", "555-101"));
        kuryeHavuzu.add(new Kurye("Mehmet", "Demir", "555-102"));
        kuryeHavuzu.add(new Kurye("Emin", "Kardas", "555-103"));
        kuryeHavuzu.add(new Kurye("Berhak", "Tanyildizi", "555-104"));

        // --- PENCERE AYARLARI ---
        setTitle("Kargo Otomasyon Sistemi");
        setSize(500, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Göz yormayan yumuşak bir arka plan (Açık Gri - White Smoke)
        getContentPane().setBackground(new Color(245, 245, 245)); 

        // --- HEADER (ÜST KISIM) ---
        JPanel headerPanel = new JPanel();
        // Daha mat ve profesyonel bir koyu mavi-gri
        headerPanel.setBackground(new Color(44, 62, 80)); 
        headerPanel.setBorder(new EmptyBorder(25, 15, 25, 15)); 
        
        JLabel title = new JLabel("CEYLAN LOJİSTİK");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subTitle = new JLabel("Otomasyon Yönetim Paneli");
        subTitle.setForeground(new Color(189, 195, 199)); // Griye çalan beyaz
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.setLayout(new GridLayout(2, 1));
        headerPanel.add(title);
        headerPanel.add(subTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- MENÜ (ORTA KISIM) ---
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        menuPanel.setBackground(new Color(245, 245, 245)); // Arkaplanla uyumlu
        menuPanel.setBorder(new EmptyBorder(40, 60, 40, 60)); // Kenarlardan daha fazla boşluk

        // Butonlar için göz yormayan, güven veren tek bir renk seçtik (Sakin Mavi)
        Color buttonColor = new Color(44, 62, 80); 

        JButton btnAdd = createCustomButton("YENİ KARGO OLUŞTUR", buttonColor);
        JButton btnUpdate = createCustomButton("DURUM GÜNCELLE", buttonColor); 
        JButton btnFind = createCustomButton("KARGO SORGULA", buttonColor);
        JButton btnList = createCustomButton("TÜM LİSTE", buttonColor);

        menuPanel.add(btnAdd);
        menuPanel.add(btnUpdate);
        menuPanel.add(btnFind);
        menuPanel.add(btnList);
        add(menuPanel, BorderLayout.CENTER);

        // --- FOOTER (ALT KISIM) ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(236, 240, 241)); // Menüden bir ton koyu gri
        footerPanel.setBorder(new EmptyBorder(10,0,10,0));
        
        JLabel lblFooter = new JLabel("© 2025 Umut Ceylan | Sistem V1.0 Aktif");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFooter.setForeground(new Color(127, 140, 141)); // Koyu gri yazı
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        // --- İŞLEVLER (ACTION LISTENERS) ---
        
        // 1. EKLEME
        btnAdd.addActionListener(e -> {
            try {
                String no = JOptionPane.showInputDialog("Kargo Takip No:");
                if(no == null || no.isEmpty()) return;
                
                String wStr = JOptionPane.showInputDialog("Ağırlık (kg):");
                if(wStr == null) return;
                double weight = Double.parseDouble(wStr);

                Musteri sender = createMusteriForm("Gönderici Bilgileri");
                if(sender == null) return;
                
                Musteri receiver = createMusteriForm("Alıcı Bilgileri");
                if(receiver == null) return;

                Kargo newKargo = new Kargo(no, sender, receiver, weight);

                Random rand = new Random();
                Kurye atananKurye = kuryeHavuzu.get(rand.nextInt(kuryeHavuzu.size()));
                
                newKargo.setKurye(atananKurye);
                newKargo.setDurum(KargoDurumu.KABUL_EDILDI);

                cargoList.add(newKargo);

                String mesaj = "✅ Kargo Sisteme Girildi!\n" +
                               "👤 Atanan Kurye: " + atananKurye.getTamAd() + "\n" +
                               "📍 Durum: " + newKargo.getDurum();
                JOptionPane.showMessageDialog(this, mesaj, "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
            }
        });

        // 2. GÜNCELLEME
        btnUpdate.addActionListener(e -> {
            String no = JOptionPane.showInputDialog("Kargo No:");
            Kargo k = findKargo(no);
            if(k != null) {
                KargoDurumu secilen = (KargoDurumu) JOptionPane.showInputDialog(
                        this, "Yeni Durum:", "Güncelle", 
                        JOptionPane.QUESTION_MESSAGE, null, 
                        KargoDurumu.values(), k.getDurum());

                if(secilen != null) {
                    k.setDurum(secilen);
                    JOptionPane.showMessageDialog(this, "✅ Durum Güncellendi: " + secilen);
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ Kargo Bulunamadı!");
            }
        });

        // 3. SORGULA
        btnFind.addActionListener(e -> {
            String no = JOptionPane.showInputDialog("Kargo No:");
            Kargo k = findKargo(no);
            if(k != null) {
                String kuryeAdi = (k.getKurye() != null) ? k.getKurye().getTamAd() : "Atanmadı";
                String info = " KARGO DETAYI\n----------------\n" +
                              "Durum: " + k.getDurum() + "\n" +
                              "Gönderen: " + k.getGonderici().getTamAd() + "\n" + 
                              "Alıcı: " + k.getAlici().getTamAd() + "\n" +        
                              "Kurye: " + kuryeAdi + "\n" + 
                              "Fiyat: " + k.ucretHesapla() + " TL";
                JOptionPane.showMessageDialog(this, info);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Bulunamadı.");
            }
        });

        // 4. LİSTELE
        btnList.addActionListener(e -> {
            if (cargoList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Listede hiç kargo yok.");
                return;
            }
            StringBuilder sb = new StringBuilder("SİSTEMDEKİ KARGOLAR:\n\n");
            for(Kargo k : cargoList) {
                String kurye = (k.getKurye() != null) ? k.getKurye().getTamAd() : "-";
                sb.append(k.getTakipNo()).append(" | ").append(k.getDurum())
                  .append(" | 👤 ").append(kurye).append("\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString(), 15, 30)));
        });

        setLocationRelativeTo(null); // Ekranı ortala
        setVisible(true);
    }

    // --- ÖZEL BUTON TASARIM METODU ---
    private JButton createCustomButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); 
        btn.setBorderPainted(false); // Kenarlık kalksın (Flat tasarım)
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        return btn;
    }

    private Kargo findKargo(String no) {
        if(no == null) return null;
        for(Kargo k : cargoList) {
            if(k.getTakipNo().equalsIgnoreCase(no)) return k; 
        }
        return null;
    }

    private Musteri createMusteriForm(String title) {
        JTextField tAd = new JTextField(), tSoyad = new JTextField(), tTel = new JTextField(), tAdres = new JTextField();
        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5)); 
        p.add(new JLabel("Ad:")); p.add(tAd); 
        p.add(new JLabel("Soyad:")); p.add(tSoyad);
        p.add(new JLabel("Tel:")); p.add(tTel); 
        p.add(new JLabel("Adres:")); p.add(tAdres);
        
        int opt = JOptionPane.showConfirmDialog(null, p, title, JOptionPane.OK_CANCEL_OPTION);
        return (opt == JOptionPane.OK_OPTION) ? new Musteri(tAd.getText(), tSoyad.getText(), tTel.getText(), tAdres.getText()) : null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KargoSistemi());
    }
}