package librarymanagement;

import javax.swing.SwingUtilities;
import librarymanagement.service.LibraryService;
import librarymanagement.ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryService service = new LibraryService();
            MainFrame frame = new MainFrame(service);
            frame.setVisible(true);
        });
    }
}
