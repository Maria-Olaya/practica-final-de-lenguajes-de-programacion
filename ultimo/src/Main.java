import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;


public class Main extends JFrame {

    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 250;
    private static final int TEXT_FIELD_COLUMNS = 20;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 30;
    private static final int EMOJI_WIDTH = 30;
    private static final int EMOJI_HEIGHT = 30;

    private static JTextField textField;
    private static JTextPane textPane;
    private static Map<String, String> emojiMap;
    private static Map<String, Boolean> diccionario;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        inicializarEmojiMap();
        inicializarDiccionario();

        JFrame frame = new JFrame("Ventana Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Añadir un TitledBorder al JTextField
        textField = new JTextField(TEXT_FIELD_COLUMNS);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Ingrese Texto");
        textField.setBorder(titledBorder);
        frame.add(textField, BorderLayout.NORTH);

        JButton button = new JButton("procesar texto");
        button.addActionListener(e -> mostrarCuadroDialogo());

        // Cambiar la apariencia del botón
        button.setFont(new Font("Arial", Font.PLAIN, 14));

        // Ajustar el tamaño del botón y los márgenes
        Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT / 2);
        button.setMaximumSize(buttonSize);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMargin(new Insets(5, 10, 5, 10));

        JPanel buttonPanel = new JPanel();  // Panel adicional para manejar el tamaño del botón
        buttonPanel.add(button);

        frame.add(buttonPanel, BorderLayout.CENTER);

        textPane = new JTextPane();
        textPane.setEditable(false);

        // Establecer el estilo del JTextPane
        textPane.setFont(new Font("Arial", Font.PLAIN, 100));

        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    private static void inicializarEmojiMap() {
        emojiMap = new HashMap<>();

        emojiMap.put(":)", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\003-feliz.png");
        emojiMap.put(":(", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\015-emoji-1.png");
        emojiMap.put(":D", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\019-entusiasta.png");
        emojiMap.put("^U^", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\058-riendo.png");
        emojiMap.put("(Y)", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\020-me-gusta.png");
        emojiMap.put("(N)el", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\028-pulgares-abajo.png");
        emojiMap.put("\\\\m/", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\027-fresco.png");
        emojiMap.put(";)", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\018-guino.png");
        emojiMap.put("<3_<3", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\001-emoji.png");
        emojiMap.put(":-O", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\004-conmocionado.png");
        emojiMap.put(":O", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\004-conmocionado.png");
        emojiMap.put(":-|", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\004-conmocionado.png");
        emojiMap.put(":|", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\004-conmocionado.png");
        emojiMap.put("':|", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\033-pensando-1.png");
        emojiMap.put(">:(", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\011-enojado.png");
        emojiMap.put(":'(", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\032-llorar.png");
        emojiMap.put("^^", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\016-estrella.png");
        emojiMap.put(":-)", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\014-sonrisa.png");
        emojiMap.put(":*", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\050-enamorado.png");
        emojiMap.put("B-)", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\044-gafas-3d.png");
        emojiMap.put("U_U", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\009-triste.png");
        emojiMap.put("o_o", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\013-preocuparse.png");
        emojiMap.put(">_<", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\042-enfermo.png");
        emojiMap.put(":3-", "C:\\Users\\H´p\\IdeaProjects\\ultimo\\png\\064-perro-4.png");

    }

    private static void inicializarDiccionario() {
        diccionario = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("diccionario.txt", StandardCharsets.UTF_8))) {
            String palabra;
            while ((palabra = br.readLine()) != null) {
                diccionario.put(palabra.trim().toLowerCase(), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarCuadroDialogo() {
        String textoIngresado = textField.getText();
        String textoConEmojisReemplazados = reemplazarEmojis(textoIngresado);
        int cantidadPalabras = contarPalabras(textoIngresado);
        int cantidadEmojis = contarEmojis(textoIngresado);

        // Crear una cadena con el resultado
        StringBuilder resultado = new StringBuilder();
        resultado.append("<html>Texto con Emojis Reemplazados:<br>").append(textoConEmojisReemplazados)
                .append("<br><br>Cantidad de Palabras: ").append(cantidadPalabras)
                .append("<br>Cantidad de Emojis: ").append(cantidadEmojis).append("</html>");

        // Mostrar el resultado en el JTextPane
        setStyledText(resultado.toString());
    }

    private static void setStyledText(String htmlText) {
        textPane.setContentType("text/html");
        textPane.setText(htmlText);
    }

    private static String reemplazarEmojis(String texto) {
        String textoConEmojis = texto;

        for (Map.Entry<String, String> entry : emojiMap.entrySet()) {
            String emoji = entry.getKey();
            String rutaImagen = entry.getValue();

            if (textoConEmojis.contains(emoji)) {
                textoConEmojis = textoConEmojis.replace(emoji, getEmojiHTML(rutaImagen));
            }
        }

        return textoConEmojis;
    }

    private static String getEmojiHTML(String rutaImagen) {
        return "<img src='file:" + rutaImagen + "' width='" + EMOJI_WIDTH + "' height='" + EMOJI_HEIGHT + "'/>";
    }


    private static int contarPalabras(String texto) {
        Pattern pattern = Pattern.compile("\\b[\\p{L}'‘’]+\\b", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(texto);
        int count = 0;
        while (matcher.find()) {
            String palabra = matcher.group().toLowerCase();
            if (diccionario.containsKey(palabra)) {
                count++;
            }
        }
        return count;
    }



    private static int contarEmojis(String texto) {
        int count = 0;
        for (Map.Entry<String, String> entry : emojiMap.entrySet()) {
            String emoji = entry.getKey();
            Pattern pattern = Pattern.compile(Pattern.quote(emoji));
            Matcher matcher = pattern.matcher(texto);
            while (matcher.find()) {
                count++;
            }
        }
        return count;
    }
}
