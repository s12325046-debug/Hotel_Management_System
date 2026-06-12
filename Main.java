package com.hotelmanagement;

import com.hotelmanagement.ui.LoginFrame;
import com.hotelmanagement.ui.UIStyleUtil;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
            } catch (Exception ignored) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception fallbackIgnored) {
                    // The default Swing look and feel is acceptable if FlatLaf is unavailable.
                }
            }
            UIStyleUtil.applyGlobalDefaults();

            new LoginFrame().setVisible(true);
        });
    }
}
