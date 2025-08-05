package com.unespar.projetofisica;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Screen extends JFrame {

    JTextField cap1, cap2, cap3, vMaxInput;
    JTextField resultadoTensao, resultadoEnergia;

    public Screen() {
        setUndecorated(true);
        setSize(430, 420);
        setTitle("Capacitores em Série");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(19, 40, 43));

        JPanel titleBar = new JPanel(null);
        titleBar.setBackground(new Color(40, 40, 40));
        titleBar.setBounds(0, 0, 430, 30);

        JLabel title = new JLabel("  Capacitores em Série");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setBounds(10, 0, 200, 30);

        JButton close = new JButton("×");
        close.setBounds(380, 0, 50, 30);
        close.setFont(new Font("Arial", Font.PLAIN, 24));
        close.setForeground(Color.WHITE);
        close.setBackground(Color.RED);
        close.setFocusPainted(false);
        close.setBorderPainted(false);
        close.addActionListener(e -> System.exit(0));

        titleBar.add(title);
        titleBar.add(close);
        add(titleBar);

        // Botões
        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(160, 350, 90, 30);
        btnReset.setFont(new Font("Arial", Font.BOLD, 15));
        btnReset.setBackground(new Color(75, 185, 199));
        btnReset.setForeground(Color.WHITE);
        btnReset.addActionListener(this::resetCampos);
        add(btnReset);

        JButton btnCalcular = new JButton("Calcular");
        btnCalcular.setBounds(300, 100, 100, 30);
        btnCalcular.setFont(new Font("Arial", Font.BOLD, 15));
        btnCalcular.setBackground(new Color(75, 185, 199));
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.addActionListener(this::calcular);
        add(btnCalcular);

        // Entradas
        cap1 = criaCampoTexto(200, 60);
        cap2 = criaCampoTexto(200, 100);
        cap3 = criaCampoTexto(200, 140);
        vMaxInput = criaCampoTexto(200, 180);

        add(criaLabel("C1 (µF):", 12, 53));
        add(cap1);
        add(criaLabel("C2 (µF):", 12, 93));
        add(cap2);
        add(criaLabel("C3 (µF):", 12, 133));
        add(cap3);
        add(criaLabel("Tensão máx (V):", 12, 173));
        add(vMaxInput);

        // Saídas
        resultadoTensao = criaCampoSaida("Tensão total (V):", 60, 240);
        resultadoEnergia = criaCampoSaida("Energia total (J):", 60, 290);

        add(resultadoTensao);
        add(resultadoEnergia);
    }

    // Cria campo de texto padrão
    private JTextField criaCampoTexto(int x, int y) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, 70, 30);
        campo.setFont(new Font("Arial", Font.BOLD, 14));
        return campo;
    }

    // Cria label padrão
    private JLabel criaLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setBounds(x, y, 170, 50);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    // Cria campo de saída formatado
    private JTextField criaCampoSaida(String texto, int x, int y) {
        JTextField campo = new JTextField(texto);
        campo.setBounds(x, y, 300, 30);
        campo.setFont(new Font("Arial", Font.BOLD, 14));
        campo.setEditable(false);
        campo.setBackground(new Color(19, 40, 43));
        campo.setForeground(Color.WHITE);
        return campo;
    }

    // Limpa os campos
    private void resetCampos(ActionEvent e) {
        cap1.setText("");
        cap2.setText("");
        cap3.setText("");
        vMaxInput.setText("");
        resultadoTensao.setText("Tensão total (V):");
        resultadoEnergia.setText("Energia total (J):");
    }

    // Lógica de cálculo principal
    private void calcular(ActionEvent e) {
        try {
            // Lê valores e converte de µF para F
            double C1 = Double.parseDouble(cap1.getText()) * 1e-6;
            double C2 = Double.parseDouble(cap2.getText()) * 1e-6;
            double C3 = Double.parseDouble(cap3.getText()) * 1e-6;
            double Vmax = Double.parseDouble(vMaxInput.getText());

            if (C1 <= 0 || C2 <= 0 || C3 <= 0 || Vmax <= 0) {
                JOptionPane.showMessageDialog(this, "Todos os valores devem ser positivos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcula a carga máxima que pode ser aplicada sem ultrapassar o Vmax
            double Q1 = C1 * Vmax;
            double Q2 = C2 * Vmax;
            double Q3 = C3 * Vmax;
            double Qmin = Math.min(Q1, Math.min(Q2, Q3)); // carga mais limitante

            // Calcula a tensão total: V = Q/C para cada, somando tudo
            double Vtotal = Qmin / C1 + Qmin / C2 + Qmin / C3;

            // Capacitância equivalente em série
            double Ceq = 1.0 / (1.0 / C1 + 1.0 / C2 + 1.0 / C3);

            // Energia: U = 1/2 * Ceq * Vtotal²
            double energia = 0.5 * Ceq * Math.pow(Vtotal, 2);

            // Mostra os resultados
            resultadoTensao.setText("Tensão total (V): " + String.format("%.2f", Vtotal));
            resultadoEnergia.setText("Energia total (J): " + String.format("%.4f", energia));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores numéricos válidos.", "Erro de entrada", JOptionPane.ERROR_MESSAGE);
        }
    }
}