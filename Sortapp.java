import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Sortapp {
    public static void main(String[] args) {
        // Membuat frame utama
        JFrame frame = new JFrame("Stepwise Sorting Application");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Data model untuk tabel pertama (angka yang dimasukkan)
        String[] columnNames = {"Angka"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable dataTable = new JTable(tableModel);
        JScrollPane dataScrollPane = new JScrollPane(dataTable);

        // Data model untuk tabel kedua (opsi sorting)
        String[] sortColumnNames = {"Metode Sort"};
        String[][] sortOptions = {
                {"Insertion Sort"},
                {"Selection Sort"}
        };
        DefaultTableModel sortTableModel = new DefaultTableModel(sortOptions, sortColumnNames);
        JTable sortTable = new JTable(sortTableModel);
        JScrollPane sortScrollPane = new JScrollPane(sortTable);

        // Tombol untuk menambahkan angka dan mengurutkan
        JButton addButton = new JButton("Tambah Angka");
        JButton sortButton = new JButton("Urutkan (Per Iterasi)");

        // Menambahkan komponen ke frame
        frame.add(dataScrollPane, BorderLayout.CENTER);
        frame.add(sortScrollPane, BorderLayout.EAST);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(sortButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // List untuk menyimpan angka sementara
        ArrayList<Integer> numbers = new ArrayList<>();
        Random random = new Random();

        // Variabel status sorting
        SortingStatus sortingStatus = new SortingStatus();

        // Listener untuk tombol "Tambah Angka"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int randomNumber = random.nextInt(100); // Angka acak 0-99
                numbers.add(randomNumber);
                tableModel.addRow(new Object[]{randomNumber});
                sortingStatus.reset(); // Reset status sorting jika angka ditambahkan
            }
        });

        // Listener untuk tombol "Urutkan (Per Iterasi)"
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (numbers.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Tidak ada angka untuk diurutkan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int selectedRow = sortTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Pilih metode sorting terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selectedSortMethod = (String) sortTableModel.getValueAt(selectedRow, 0);
                if ("Insertion Sort".equals(selectedSortMethod)) {
                    performInsertionSortStep(numbers, sortingStatus);
                } else if ("Selection Sort".equals(selectedSortMethod)) {
                    performSelectionSortStep(numbers, sortingStatus);
                }

                // Perbarui tabel dengan data setelah satu iterasi sorting
                updateTable(tableModel, numbers);

                if (sortingStatus.isComplete()) {
                    JOptionPane.showMessageDialog(frame, "Sorting selesai!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Tampilkan frame
        frame.setVisible(true);
    }

    // Method untuk mengupdate tabel dengan data baru
    private static void updateTable(DefaultTableModel tableModel, ArrayList<Integer> numbers) {
        tableModel.setRowCount(0); // Kosongkan tabel
        for (Integer number : numbers) {
            tableModel.addRow(new Object[]{number});
        }
    }

    // Insertion Sort per iterasi
    private static void performInsertionSortStep(ArrayList<Integer> list, SortingStatus status) {
        if (status.isComplete()) {
            return;
        }

        int i = status.getCurrentIndex();
        int key = list.get(i);
        int j = i - 1;

        while (j >= 0 && list.get(j) > key) {
            list.set(j + 1, list.get(j));
            j--;
        }
        list.set(j + 1, key);

        status.next(); // Lanjutkan ke iterasi berikutnya
    }

    // Selection Sort per iterasi
    private static void performSelectionSortStep(ArrayList<Integer> list, SortingStatus status) {
        if (status.isComplete()) {
            return;
        }

        int i = status.getCurrentIndex();
        int minIndex = i;

        for (int j = i + 1; j < list.size(); j++) {
            if (list.get(j) < list.get(minIndex)) {
                minIndex = j;
            }
        }

        // Tukar elemen
        int temp = list.get(minIndex);
        list.set(minIndex, list.get(i));
        list.set(i, temp);

        status.next(); // Lanjutkan ke iterasi berikutnya
    }
}

// Class untuk menyimpan status sorting
class SortingStatus {
    private int currentIndex = 1;
    private boolean complete = false;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isComplete() {
        return complete;
    }

    public void next() {
        currentIndex++;
        if (currentIndex >= Integer.MAX_VALUE) {
            complete = true;
        }
    }

    public void reset() {
        currentIndex = 1;
        complete = false;
    }
}
