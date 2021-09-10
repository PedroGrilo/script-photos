import com.formdev.flatlaf.FlatLightLaf;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import duplicatedFiles.DuplicatedFiles;
import duplicatedFiles.OSType;
import file.FileProperties;
import file.Tools;
import organizeFiles.Organizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SortingMemories {
    private static final String CONFIRMATION_EMPTY_FOLDERS = " empty folders were deleted.";
    private static final String WARNING_ORGANIZING = "WARNING: if you want that some photos or folders to NOT being organized by year,\nput them on a folder named 'not organize' in the root folder. Continue?";
    private static final String ORGANIZING = "Organizing photos...";
    private static final String ORGANIZED = " photos were organized";
    private static final String CHOOSE_APP = "Please choose an application to open pictures\n(if the photos don't show up reboot the program and choose another app):";
    private static final String DESKTOP = "\\.desktop";
    private static final String VIDEOS = "Videos and others:";
    private static final String INSERT_FOLDER = "Please insert a folder's directory";
    private static final String LOADING_FILES = "Loading all files metadata...";
    private static final String WARNING_STAGES = "WARNING: We will divide the images whole process into 9 stages, so how this is a long process you can execute them separately.\n" +
            " The files you'll choose to delete will be moved to a folder named 'to delete', this is a security procedure,\n so at the end you will only have to delete the folder." +
            " If you already have a folder with this name please rename it. Do you want to continue?";
    private static final String WRONG_SELECTION = "Choose just images stages or just videos stages.";
    private static final String WRONG_PERCENTAGE = "Wrong percentage value.";
    private static final int MULTIPLICATION = 5;

    private JPanel rootPanel;
    private static JFrame frame;
    private JTextField textDir;
    private JButton searchButton;
    private JButton emptyFolders;
    private JButton duplicatedFiles;
    private JButton organize;
    private JTextArea log;
    private JTextField textPercentage;
    private JPanel panel1;
    private JButton continueButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;
    private JCheckBox checkBox7;
    private JCheckBox checkBox8;
    private JCheckBox checkBox9;
    private JCheckBox checkBox10;
    private JPanel panel2;
    private JButton nextButton;
    private JTable table1;
    private JLabel logo;
    private JPanel progressBarPanel;
    private JProgressBar progressBar1;
    private JLabel pBarDesc;
    private JScrollPane logPanel;
    private File folder;
    private final List<JCheckBox> checkBoxes;
    private DuplicatedFiles df;
    private Iterator<Map.Entry<Integer, CopyOnWriteArrayList<FileProperties>>> groups;
    private int size;
    private int master;
    private int index;

    public SortingMemories() {
        $$$setupUI$$$();
        panel1.setVisible(false);
        panel2.setVisible(false);
        logPanel.setVisible(false);
        progressBarPanel.setVisible(false);
        checkBoxes = new ArrayList<>();
        checkBoxes.add(checkBox1);
        checkBoxes.add(checkBox2);
        checkBoxes.add(checkBox3);
        checkBoxes.add(checkBox4);
        checkBoxes.add(checkBox5);
        checkBoxes.add(checkBox6);
        checkBoxes.add(checkBox7);
        checkBoxes.add(checkBox8);
        checkBoxes.add(checkBox9);
        checkBoxes.add(checkBox10);
        nextButton.setBackground(Color.CYAN);
        continueButton.setBackground(Color.CYAN);
        searchButton.setBackground(Color.CYAN);
        duplicatedFiles.setBackground(Color.lightGray);
        organize.setBackground(Color.lightGray);
        emptyFolders.setBackground(Color.lightGray);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.showSaveDialog(null);
                folder = f.getSelectedFile();
                textDir.setText(folder.getAbsolutePath());
            }
        });
        emptyFolders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkDirectory();
                    frame.setAlwaysOnTop(false);
                    logPanel.setVisible(false);
                    panel1.setVisible(false);
                    panel2.setVisible(false);
                    progressBarPanel.setVisible(false);
                    textDir.setEnabled(true);
                    searchButton.setEnabled(true);

                    JOptionPane.showMessageDialog(frame, Tools.deleteEmptyFolders(folder, 0) + CONFIRMATION_EMPTY_FOLDERS);
                } catch (ScriptException scriptException) {
                    scriptException.printStackTrace();
                }
            }
        });
        organize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkDirectory();
                    frame.setAlwaysOnTop(false);
                    logPanel.setVisible(true);
                    panel1.setVisible(false);
                    panel2.setVisible(false);
                    progressBarPanel.setVisible(false);
                    textDir.setEnabled(true);
                    searchButton.setEnabled(true);
                    log.setText("");
                    int res = JOptionPane.showConfirmDialog(frame, WARNING_ORGANIZING, "Warning about organizing files", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        int counter = 0;
                        log.append("Log:\n");
                        log.append(ORGANIZING + "\n");
                        Iterator<String> it = (new Organizer(folder)).organizeFiles();

                        while (it.hasNext()) {
                            log.append(it.next() + "\n");
                            counter++;
                        }
                        int deleted = Tools.deleteEmptyFolders(folder, 0);
                        JOptionPane.showMessageDialog(frame, counter + ORGANIZED + "\n" + deleted + CONFIRMATION_EMPTY_FOLDERS);
                        //martelada!!!!!!1
                        frame.setSize(800, 601);
                        frame.setSize(800, 600);
                        /////////////////////////
                    }
                } catch (ScriptException scriptException) {
                    scriptException.printStackTrace();
                }
            }
        });
        duplicatedFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkDirectory();
                    textDir.setEnabled(false);
                    frame.setAlwaysOnTop(false);
                    searchButton.setEnabled(false);
                    panel2.setVisible(false);
                    panel1.setVisible(false);
                    progressBarPanel.setVisible(false);
                    logPanel.setVisible(false);
                    log.setText("");
                    int res = JOptionPane.showConfirmDialog(frame, WARNING_STAGES, "Warning", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        df = new DuplicatedFiles(folder);
                        new SwingWorker<Void, String>() {
                            @Override
                            protected Void doInBackground() throws InterruptedException {
                                int sumVideos = deleteDuplicatedFiles();
                                printStages(sumVideos);
                                return null;
                            }

                            @Override
                            protected void done() {
                                panel1.setVisible(true);
                            }
                        }.execute();
                    }
                } catch (ScriptException scriptException) {
                    scriptException.printStackTrace();
                }
            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean exists = false;
                    boolean all = true;
                    for (int i = 0; i < checkBoxes.size() - 1; i++) {
                        if (checkBoxes.get(i).isSelected())
                            exists = true;
                        else
                            all = false;
                    }

                    if (checkBox10.isSelected()) {
                        if (exists) {
                            JOptionPane.showMessageDialog(frame, WRONG_SELECTION);
                            throw new ScriptException("");
                        }
                        df.setIsImage(false);

                    } else if (all) {
                        df.setIsImage(true);
                        df.addStage(-1);
                    } else {
                        df.setIsImage(true);
                        for (int i = 0; i < checkBoxes.size() - 1; i++) {
                            if (checkBoxes.get(i).isSelected())
                                df.addStage(i + 1);
                        }
                    }

                    int percentage = getPercentage();
                    panel1.setVisible(false);

                    if (df.getIsImage()) {
                        if (df.getOSType() == OSType.MACOS)
                            df.setApp("open");
                        else if (df.getOSType() == OSType.LINUX)
                            chooseImagesApp();
                        new SwingWorker<Void, String>() {
                            @Override
                            protected Void doInBackground() throws InterruptedException {
                                iteratingMaps(percentage);
                                return null;
                            }
                        }.execute();
                    } else {
                        new SwingWorker<Void, String>() {
                            @Override
                            protected Void doInBackground() throws InterruptedException {
                                iteratingMaps(-1);
                                return null;
                            }
                        }.execute();
                    }

                } catch (ScriptException | IOException scriptException) {
                    scriptException.printStackTrace();
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder answer = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    if ((boolean) table1.getValueAt(i, 0))
                        answer.append(i + 1).append(" ");
                }

                log.append(df.analyzeAnswer(answer.toString(), master));
                try {
                    df.closePreviews(master);
                    frame.setAlwaysOnTop(false);
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
                chooseToDelete();
            }
        });
    }

    private void checkDirectory() throws ScriptException {
        if (folder == null) {
            File aux = new File(textDir.getText());
            if (!aux.isDirectory()) {
                JOptionPane.showMessageDialog(frame, INSERT_FOLDER);
                throw new ScriptException(INSERT_FOLDER);
            } else
                folder = aux;
        }
    }

    private int getPercentage() throws ScriptException {
        String answer = textPercentage.getText();
        if (answer.matches("[0-9]+")) {
            int percentage = Integer.parseInt(answer);
            if (percentage >= 0 && percentage <= 100)
                return percentage;
            JOptionPane.showMessageDialog(frame, WRONG_PERCENTAGE);
            throw new ScriptException("");
        }
        JOptionPane.showMessageDialog(frame, WRONG_PERCENTAGE);
        throw new ScriptException("");
    }

    private void printStages(int sumVideos) {
        Iterator<List<FileProperties>> it = df.getAllImages();
        for (int i = 0; i < 9; i++) {
            int size = it.next().size();
            JCheckBox cb = checkBoxes.get(i);
            cb.setText("Stage " + (i + 1) + ": " + size + " images");
            cb.setEnabled(size != 0);
        }
        checkBox10.setText("Stage V - Total videos and others: " + sumVideos + " files");
        checkBox10.setEnabled(sumVideos != 0);
    }

    private void setProgressBar(String text, int max) {
        DefaultBoundedRangeModel model = new DefaultBoundedRangeModel();
        model.setMinimum(0);
        model.setMaximum(max);
        model.setValue(0);
        progressBar1.setModel(model);
        progressBar1.setPreferredSize(new Dimension(250, 20));
        progressBar1.setStringPainted(true);
        pBarDesc.setText(text);
        progressBarPanel.setVisible(true);
    }

    private void chooseImagesApp() throws IOException {
        String scriptLoc = df.permissionApp();
        Iterator<String> it = df.apps(scriptLoc);
        List<String> list = new ArrayList<>();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(CHOOSE_APP));

        List<JRadioButton> buttons = new ArrayList<>();
        ButtonGroup group = new ButtonGroup();

        int counter = 0;
        while (it.hasNext()) {
            String app = it.next().split(DESKTOP)[0];
            if (!app.equals("")) {
                counter++;
                list.add(app);
                JRadioButton cb = new JRadioButton();
                cb.setText("[" + counter + "]" + " - " + app);
                if (counter == 1)
                    cb.setSelected(true);
                buttons.add(cb);
                group.add(cb);
                panel.add(cb);
            }
        }

        JOptionPane.showMessageDialog(null, panel);

        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isSelected()) {
                df.setApp(list.get(i));
            }
        }
    }

    private int deleteDuplicatedFiles() throws InterruptedException {
        AtomicInteger sumVideos = new AtomicInteger();
        int total = (int) df.fileCount(folder.toPath());
        AtomicInteger counter = new AtomicInteger();
        File[] array = folder.listFiles();
        assert array != null;
        List<File> files = new ArrayList<>(Arrays.asList(array));
        int processors = Runtime.getRuntime().availableProcessors() * MULTIPLICATION;
        int portion;
        if (files.size() <= processors)
            portion = 1;
        else
            portion = (files.size()) / processors;
        List<Thread> threads = new ArrayList<>();
        AtomicInteger aux = new AtomicInteger();

        setProgressBar(LOADING_FILES, total);
        for (int i = 0; i < Math.min(files.size(), processors); i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                int begin = finalI * portion, end;
                if (finalI == Math.min(files.size(), processors) - 1)
                    end = files.size();
                else
                    end = begin + portion;
                for (File f : files.subList(begin, end)) {
                    aux.getAndIncrement();
                    Iterator<File> it = Tools.getFile(f, new ArrayList<>());
                    while (it.hasNext()) {
                        File file = it.next();
                        counter.getAndIncrement();
                        synchronized (this) {
                            progressBar1.setValue(counter.get());
                        }
                        sumVideos.addAndGet(df.deleteDuplicatedFiles(file));
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread t : threads)
            t.join();

        assert aux.get() == files.size();
        progressBarPanel.setVisible(false);
        return sumVideos.get();
    }

    private void iteratingMaps(int percentage) throws InterruptedException {
        int stage = 0;
        FileProperties fileP;
        Iterator<List<FileProperties>> it;

        if (df.getIsImage())
            it = df.getAllImages();
        else
            it = df.getAllVideos();

        logPanel.setVisible(true);
        while (it.hasNext()) {
            stage++;
            List<FileProperties> files = it.next();
            int processors = Runtime.getRuntime().availableProcessors() * MULTIPLICATION;
            if (df.hasStage(stage) || df.hasStage(-1) || !df.getIsImage()) {
                log.append("Loading stage " + stage + "...\n");
                setProgressBar("Loading stage " + stage + "... (1/2)\n", files.size());

                if (df.getIsImage()) {
                    int portion;
                    if (files.size() <= processors)
                        portion = 1;
                    else
                        portion = (files.size()) / processors;
                    List<Thread> threads = new ArrayList<>();
                    AtomicInteger counter = new AtomicInteger();

                    for (int i = 0; i < Math.min(files.size(), processors); i++) {
                        int finalI = i;
                        Thread thread = new Thread(() -> {
                            int begin = finalI * portion, end;
                            if (finalI == Math.min(files.size(), processors) - 1)
                                end = files.size();
                            else
                                end = begin + portion;
                            for (FileProperties fp : files.subList(begin, end)) {
                                counter.getAndIncrement();
                                synchronized (this) {
                                    progressBar1.setValue(counter.get());
                                }
                                df.definingHash(fp);
                            }
                        });
                        threads.add(thread);
                        thread.start();
                    }

                    for (Thread t : threads)
                        t.join();
                    assert counter.get() == files.size();
                    progressBarPanel.setVisible(false);
                } else
                    log.append(VIDEOS + "\n");

                progressBarPanel.setVisible(true);
                log.append("Comparing files from stage " + stage + "...\n");
                setProgressBar("Comparing files... (2/2)", files.size() - 1);
                for (int i = 0; i < files.size() - 1; i++) {
                    fileP = files.get(i);
                    if (!fileP.getSeen()) {
                        df.createChoosingGroup(i);
                        AtomicBoolean found = new AtomicBoolean(false);
                        int portion;
                        int nFiles = files.size() - i - 1;
                        if (nFiles <= processors)
                            portion = 1;
                        else
                            portion = nFiles / processors;
                        List<Thread> threads = new ArrayList<>();

                        int finalI = i;
                        FileProperties finalFileP = fileP;
                        AtomicInteger counter = new AtomicInteger();

                        for (int k = 0; k < Math.min(processors, nFiles); k++) {
                            int finalK = k;
                            Thread thread = new Thread(() -> {
                                int begin = finalI + 1 + (finalK * portion), end;
                                if (finalK == Math.min(processors, nFiles) - 1)
                                    end = files.size();
                                else
                                    end = begin + portion;

                                for (FileProperties f : files.subList(begin, end)) {
                                    counter.getAndIncrement();
                                    if (df.compareFiles(finalFileP, f, percentage, finalI))
                                        found.set(true);
                                }
                            });
                            threads.add(thread);
                            thread.start();
                        }

                        for (Thread t : threads)
                            t.join();

                        assert nFiles == counter.get();

                        if (found.get())
                            df.addToChoosingGroup(i, fileP);
                        else
                            df.removeChoosingGroup(i);

                        System.gc();
                    }
                    progressBar1.setValue(i);
                }
            }
        }
        groups = df.getToDelete();
        progressBarPanel.setVisible(false);
        log.setText("");
        if (!groups.hasNext()) {
            logPanel.setVisible(false);
            panel2.setVisible(false);
            JOptionPane.showMessageDialog(null, "There aren't duplicated files!");
            textDir.setEnabled(true);
            searchButton.setEnabled(true);
        } else {
            log.append("Files to be \"deleted\":\n");
            panel2.setVisible(true);
            index = 1;
            chooseToDelete();
        }
    }

    private void chooseToDelete() {
        if (index == df.getChoosingGroupsSize())
            nextButton.setText("Finish");
        else
            nextButton.setText("Next (" + index + "/" + df.getChoosingGroupsSize() + ")");
        if (groups.hasNext()) {
            Map.Entry<Integer, CopyOnWriteArrayList<FileProperties>> entry = groups.next();
            CopyOnWriteArrayList<FileProperties> list = entry.getValue();
            index++;

            DefaultTableModel model = new DefaultTableModel(new Object[]{"Delete?", "File path"}, 0) {
                @Override
                public Class getColumnClass(int columnIndex) {
                    if (columnIndex == 0)
                        return Boolean.class;
                    return String.class;
                }
            };

            for (FileProperties fp : list) {
                String text = fp.getFile().getAbsolutePath();
                model.addRow(new Object[]{false, text});
                df.showPicture(fp);
            }

            frame.setAlwaysOnTop(true);
            table1.setModel(model);
            table1.getColumn(table1.getColumnName(0)).setMaxWidth(60);
            size = list.size();
            master = entry.getKey();

        } else {
            df.deleteFiles();
            panel2.setVisible(false);
            textDir.setEnabled(true);
            searchButton.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Selected files were \"deleted\"!");
        }
    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatLightLaf());
        frame = new JFrame("Sorting Memories");
        frame.setContentPane(new SortingMemories().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(SortingMemories.class.getResource("logo.png"))).getImage());
        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        try {
            BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResource("logo.png")));
            logo = new JLabel(new ImageIcon(img.getScaledInstance(50, 50,
                    Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        rootPanel.setEnabled(true);
        Font rootPanelFont = this.$$$getFont$$$("Monospaced", -1, -1, rootPanel.getFont());
        if (rootPanelFont != null) rootPanel.setFont(rootPanelFont);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        logPanel = new JScrollPane();
        panel3.add(logPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 100), null, 0, false));
        log = new JTextArea();
        log.setEditable(false);
        log.setEnabled(true);
        logPanel.setViewportView(log);
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(13, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Enter the equality percentage to compare the photos:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Select the pretended stages:");
        panel1.add(label2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox1 = new JCheckBox();
        checkBox1.setText("CheckBox");
        panel1.add(checkBox1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox2 = new JCheckBox();
        checkBox2.setText("CheckBox");
        panel1.add(checkBox2, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox3 = new JCheckBox();
        checkBox3.setText("CheckBox");
        panel1.add(checkBox3, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox4 = new JCheckBox();
        checkBox4.setText("CheckBox");
        panel1.add(checkBox4, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox5 = new JCheckBox();
        checkBox5.setText("CheckBox");
        panel1.add(checkBox5, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox6 = new JCheckBox();
        checkBox6.setText("CheckBox");
        panel1.add(checkBox6, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox7 = new JCheckBox();
        checkBox7.setText("CheckBox");
        panel1.add(checkBox7, new GridConstraints(8, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox8 = new JCheckBox();
        checkBox8.setText("CheckBox");
        panel1.add(checkBox8, new GridConstraints(9, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox9 = new JCheckBox();
        checkBox9.setText("CheckBox");
        panel1.add(checkBox9, new GridConstraints(10, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkBox10 = new JCheckBox();
        checkBox10.setText("CheckBox");
        panel1.add(checkBox10, new GridConstraints(11, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textPercentage = new JTextField();
        panel1.add(textPercentage, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        continueButton = new JButton();
        continueButton.setText("Continue");
        panel1.add(continueButton, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Duplicated files have been found, please choose the ones you want to delete:");
        panel2.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextButton = new JButton();
        nextButton.setText("Next");
        panel2.add(nextButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        progressBarPanel = new JPanel();
        progressBarPanel.setLayout(new GridBagLayout());
        panel3.add(progressBarPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pBarDesc = new JLabel();
        pBarDesc.setText("");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        progressBarPanel.add(pBarDesc, gbc);
        progressBar1 = new JProgressBar();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        progressBarPanel.add(progressBar1, gbc);
        logo.setHorizontalAlignment(0);
        logo.setText("");
        rootPanel.add(logo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(10, 0, 10, 0), -1, -1));
        rootPanel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        emptyFolders = new JButton();
        emptyFolders.setText("Delete Empty Folders");
        panel4.add(emptyFolders, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        duplicatedFiles = new JButton();
        duplicatedFiles.setText("Delete Duplicated Files");
        panel4.add(duplicatedFiles, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        organize = new JButton();
        organize.setText("Organize Files By Year");
        panel4.add(organize, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 0, 0), -1, -1));
        rootPanel.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, 1, null, null, null, 0, false));
        textDir = new JTextField();
        textDir.setEnabled(true);
        textDir.setText("(Root directory)");
        panel5.add(textDir, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setHorizontalAlignment(0);
        searchButton.setText("Search");
        panel5.add(searchButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}