package Sistema;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class VisualConexion extends javax.swing.JFrame {

    Conexion con = Conexion.getInstance(); // Sólo una conexión por el patrón "Singleton"

    //Un Arduino y una conexión (Patrón Singleton)
    PanamaHitek_Arduino arduino = new PanamaHitek_Arduino();

    SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent spe) {
            try {
                if (arduino.isMessageAvailable()) {
                    String UID = arduino.printMessage();
                    System.out.println(UID);

                    char opcion = UID.charAt(UID.length() - 1); // LA ÚLTIMA LETRA PARA SABER LA ACCIÓN A REALIZAR
                    UID = UID.substring(0, UID.length() - 1);

                    if (opcion == 'A') { //Asignar

                        System.out.println("A\n");
                        asignarUID(dni_text.getText(), UID);

                    } else {

                        System.out.println("C\n"); //Comprobar
                        String dni = tarjetaEnSistema(UID);

                        if (dni != null) {

                            if (pagoActivo(dni)) {
                                try {
                                    arduino.sendData("j_active");
                                } catch (ArduinoException ex) {
                                    JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
                                } catch (SerialPortException ex) {
                                    JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
                                }

                            } else {
                                try {
                                    arduino.sendData("j_inactive");
                                } catch (ArduinoException ex) {
                                    JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
                                } catch (SerialPortException ex) {
                                    JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
                                }
                            }

                        } else {
                            try {
                                arduino.sendData("j_inactive");
                            } catch (ArduinoException ex) {
                                JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
                            } catch (SerialPortException ex) {
                                JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
                            }
                        }
                    }
                }
            } catch (ArduinoException | SerialPortException ex) {
                JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
            }
        }

        private String tarjetaEnSistema(String UID) {

            String dni_usuario = null;
            String sql = "SELECT DNI FROM CLIENTE WHERE UID = ?";

            try {

                Connection conexion = con.conectar();
                PreparedStatement preparedStatement = conexion.prepareStatement(sql);
                preparedStatement.setString(1, UID);

                ResultSet resultset = preparedStatement.executeQuery();

                if (resultset.next()) {
                    dni_usuario = resultset.getString("DNI");
                }

                if (dni_usuario != null) {
                    System.out.println("La tarjeta se encuentra en el sistema, y esta asociada a " + dni_usuario + "\n");
                } else {
                    System.out.println("La tarjeta es ajena al sistema\n");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Fallo al recoger el usuario con ese ID : " + ex);
            }

            return dni_usuario;
        }

        private void asignarUID(String dni, String UID) {

            PreparedStatement insertar1;
            PreparedStatement insertar2;

            String dni_a_poner_a_null = tarjetaEnSistema(UID);

            if (dni_a_poner_a_null != null) {
                try {

                    Connection conexion = con.conectar();

                    insertar1 = conexion.prepareStatement("UPDATE CLIENTE SET UID = ? WHERE dni = ?");
                    insertar1.setNull(1, java.sql.Types.VARCHAR);
                    insertar1.setString(2, dni_a_poner_a_null);
                    insertar1.executeUpdate();

                    con.cierreConexion();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Fallo al asignar UID de tarjeta al usuario : " + ex);
                }
            }

            try {

                Connection conexion = con.conectar();

                insertar2 = conexion.prepareStatement("UPDATE CLIENTE SET UID = ? WHERE dni = ?");
                insertar2.setString(1, UID);
                insertar2.setString(2, dni);
                insertar2.executeUpdate();

                con.cierreConexion();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Fallo al asignar UID de tarjeta al usuario : " + ex);
            }
        }

        private boolean pagoActivo(String dni) {

            boolean pagoActivo = false;
            String sql = "SELECT Pagado FROM PAGO WHERE DNI = ? ORDER BY Fecha_pago DESC LIMIT 1";

            try {

                Connection conexion = con.conectar();
                PreparedStatement preparedStatement = conexion.prepareStatement(sql);
                preparedStatement.setString(1, dni);

                ResultSet resultset = preparedStatement.executeQuery();
                if (resultset.next()) {
                    pagoActivo = resultset.getBoolean("Pagado");
                }

                if (pagoActivo) {
                    System.out.println("El pago está activo\n");
                } else {
                    System.out.println("El pago está inactivo\n");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Fallo al recoger el usuario con ese ID : " + ex);
            }

            return pagoActivo;

        }
    };

    public VisualConexion() {
        initComponents();

        //Configuración Arduino
        try {
            //Así podemos mandar y recibir información RX --> RECEPCIÓN TX --> TRANSMITIR de Java a Arduino
            arduino.arduinoRXTX("COM3", 9600, listener);
        } catch (ArduinoException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexión con el lector : " + ex);

        }

        this.setLocationRelativeTo(null);
        this.setTitle("Control de acceso");
        setIconImage(new ImageIcon(getClass().getResource("../icon/chapa.png")).getImage());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cerrar = new javax.swing.JButton();
        abrir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        dni_text = new javax.swing.JTextField();
        asignar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ASIGNAR TARJETA A USUARIO");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 440, 580, 53));

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("OPCIONES TORNO");
        jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 420, 53));

        cerrar.setBackground(new java.awt.Color(255, 102, 102));
        cerrar.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 18)); // NOI18N
        cerrar.setForeground(new java.awt.Color(0, 0, 0));
        cerrar.setText("CERRAR");
        cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarActionPerformed(evt);
            }
        });
        jPanel1.add(cerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 270, 220, 120));

        abrir.setBackground(new java.awt.Color(102, 255, 102));
        abrir.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 18)); // NOI18N
        abrir.setForeground(new java.awt.Color(0, 0, 0));
        abrir.setText("ABRIR");
        abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirActionPerformed(evt);
            }
        });
        jPanel1.add(abrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 220, 110));

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DNI Usuario");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 490, -1, -1));

        dni_text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dni_textActionPerformed(evt);
            }
        });
        jPanel1.add(dni_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 550, 348, 37));

        asignar.setBackground(new java.awt.Color(51, 51, 51));
        asignar.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 18)); // NOI18N
        asignar.setForeground(new java.awt.Color(255, 255, 255));
        asignar.setText("ASIGNAR");
        asignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asignarActionPerformed(evt);
            }
        });
        jPanel1.add(asignar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 600, 520, 100));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 440, 70));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 440, 570, 90));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/chapa.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 800));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void asignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asignarActionPerformed
        try {
            arduino.sendData("j_start");
        } catch (ArduinoException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
        } catch (SerialPortException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
        }
    }//GEN-LAST:event_asignarActionPerformed

    private void dni_textActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dni_textActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dni_textActionPerformed

    private void abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirActionPerformed
        try {
            arduino.sendData("j_active");
        } catch (ArduinoException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
        } catch (SerialPortException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
        }
    }//GEN-LAST:event_abrirActionPerformed

    private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarActionPerformed
        try {
            arduino.sendData("j_inactive");
        } catch (ArduinoException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
        } catch (SerialPortException ex) {
            JOptionPane.showMessageDialog(null, "Error en la conexión serial : " + ex);
        }
    }//GEN-LAST:event_cerrarActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VisualConexion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VisualConexion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VisualConexion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VisualConexion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VisualConexion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abrir;
    private javax.swing.JButton asignar;
    private javax.swing.JButton cerrar;
    private javax.swing.JTextField dni_text;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
