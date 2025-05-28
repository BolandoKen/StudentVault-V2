import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CSearchPanels {

   
    public static class CollegeSearchPanel extends JPanel {
            private JTextField searchField;
            private JComboBox<String> searchColumnComboBox;
            private JButton searchButton;
            private JButton clearButton;
            private Consumer<String[]> searchCallback;
            private Timer searchTimer;
    
            public CollegeSearchPanel(Consumer<String[]> searchCallback) {
                System.out.println("CollegeSearchPanel: Constructor called with callback");
                this.searchCallback = searchCallback;
                initializeUI();
            }
    
            public CollegeSearchPanel() {
                System.out.println("CollegeSearchPanel: Constructor called without callback");
                initializeUI();
            }
    
            private void initializeUI() {
                System.out.println("CollegeSearchPanel: Initializing UI");
                setLayout(new FlowLayout(FlowLayout.LEFT));
                setBorder(BorderFactory.createTitledBorder("Search Colleges"));
    
                searchField = new JTextField(20);
    
                searchField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        System.out.println("CollegeSearchPanel: Text inserted, triggering search");
                        triggerDelayedSearch();
                    }
    
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        System.out.println("CollegeSearchPanel: Text removed, triggering search");
                        triggerDelayedSearch();
                    }
    
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        System.out.println("CollegeSearchPanel: Text changed, triggering search");
                        triggerDelayedSearch();
                    }
                });
    
                searchColumnComboBox = new JComboBox<>(new String[]{"All", "College Code", "College Name"});
                searchColumnComboBox.addActionListener(e -> {
                    System.out.println("CollegeSearchPanel: ComboBox selection changed");
                    performSearch();
                });
    
                searchButton = new JButton("Search");
                searchButton.addActionListener(e -> {
                    System.out.println("CollegeSearchPanel: Search button clicked");
                    performSearch();
                });
    
                clearButton = new JButton("Clear");
                clearButton.addActionListener(e -> {
                    System.out.println("CollegeSearchPanel: Clear button clicked");
                    searchField.setText("");
                    searchColumnComboBox.setSelectedIndex(0);
                    performSearch();
                });
    
                add(new JLabel("Search:"));
                add(searchField);
                add(new JLabel("in"));
                add(searchColumnComboBox);
                add(searchButton);
                add(clearButton);
    
                searchTimer = new Timer(300, e -> {
                    System.out.println("CollegeSearchPanel: Timer triggered, performing search");
                    performSearch();
                });
                searchTimer.setRepeats(false);
                
                System.out.println("CollegeSearchPanel: UI initialization complete");
            }
    
            private void triggerDelayedSearch() {
                System.out.println("CollegeSearchPanel: triggerDelayedSearch called");
                if (searchTimer.isRunning()) {
                    System.out.println("CollegeSearchPanel: Stopping existing timer");
                    searchTimer.stop();
                }
                System.out.println("CollegeSearchPanel: Starting new timer");
                searchTimer.start();
            }
    
            public void setSearchCallback(Consumer<String[]> searchCallback) {
                System.out.println("CollegeSearchPanel: setSearchCallback called");
                this.searchCallback = searchCallback;
            }
    
            private void performSearch() {
                System.out.println("CollegeSearchPanel: performSearch called");
                
                if (searchCallback == null) {
                    System.out.println("CollegeSearchPanel: ERROR - searchCallback is null!");
                    return;
                }
    
                String searchText = searchField.getText().trim();
                String columnName = (String) searchColumnComboBox.getSelectedItem();
                
                System.out.println("CollegeSearchPanel: Search text: '" + searchText + "'");
                System.out.println("CollegeSearchPanel: Column name: '" + columnName + "'");
                System.out.println("CollegeSearchPanel: Calling callback...");
                
                searchCallback.accept(new String[]{searchText, columnName});
                System.out.println("CollegeSearchPanel: Callback called successfully");
            }
            
            public void setSearchText(String text) {
                searchField.setText(text);
            }
    
            public void setSearchColumn(String columnName) {
                for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
                    if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                        searchColumnComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
    
            public void clearSearch() {
                searchField.setText("");
                searchColumnComboBox.setSelectedIndex(0);
                performSearch();
            }
    
            public void executeSearch() {
                performSearch();
            }
    
            public String getSearchText() {
                return searchField.getText();
            }
    
            public String getSelectedColumn() {
                return (String) searchColumnComboBox.getSelectedItem();
            }
        }

    public static class ProgramSearchPanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> searchColumnComboBox;
    private JButton searchButton;
    private JButton clearButton;
    private Consumer<String[]> searchCallback;
    private Timer searchTimer;

    public ProgramSearchPanel(Consumer<String[]> searchCallback) {
        this.searchCallback = searchCallback;
        initializeUI();
    }

    public ProgramSearchPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Search Programs"));

      
        searchField = new JTextField(20);
 
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                triggerDelayedSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                triggerDelayedSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                triggerDelayedSearch();
            }
        });

        searchColumnComboBox = new JComboBox<>(new String[]{"All", "Program Code", "Program Name", "College Code"});
        searchColumnComboBox.addActionListener(e -> performSearch());

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchColumnComboBox.setSelectedIndex(0);
            performSearch();
        });

        add(new JLabel("Search:"));
        add(searchField);
        add(new JLabel("in"));
        add(searchColumnComboBox);
        add(searchButton);
        add(clearButton);

       searchTimer = new Timer(300, e -> {
        System.out.println("Timer triggered search"); // Debug line
         performSearch();
        });
        searchTimer.setRepeats(false);
    }

    private void triggerDelayedSearch() {
        if (searchTimer.isRunning()) {
            searchTimer.stop();
        }
        searchTimer.start();
    }

    public void setSearchCallback(Consumer<String[]> searchCallback) {
        this.searchCallback = searchCallback;
    }

    private void performSearch() {
        if (searchCallback == null) {
            return;
        }

        String searchText = searchField.getText().trim();
        String columnName = (String) searchColumnComboBox.getSelectedItem();
        
        searchCallback.accept(new String[]{searchText, columnName});
    }

    public void setSearchText(String text) {
        searchField.setText(text);
    }

    public void setSearchColumn(String columnName) {
        for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
            if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                searchColumnComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    public void clearSearch() {
        searchField.setText("");
        searchColumnComboBox.setSelectedIndex(0);
        performSearch();
    }

    public void executeSearch() {
        performSearch();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public String getSelectedColumn() {
        return (String) searchColumnComboBox.getSelectedItem();
    }
}

    public static class StudentSearchPanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> searchColumnComboBox;
    private JButton searchButton;
    private JButton clearButton;
    private Consumer<String[]> searchCallback;
    private Timer searchTimer;

    public StudentSearchPanel(Consumer<String[]> searchCallback) {
        this.searchCallback = searchCallback;
        initializeUI();
    }

    public StudentSearchPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Search Students"));

        searchField = new JTextField(20);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                triggerDelayedSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                triggerDelayedSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                triggerDelayedSearch();
            }
        });

        searchColumnComboBox = new JComboBox<>(new String[]{"All", "First Name", "Last Name", "ID Number", "Year Level", "Program"});
        searchColumnComboBox.addActionListener(e -> performSearch());

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchColumnComboBox.setSelectedIndex(0);
            performSearch();
        });

        add(new JLabel("Search:"));
        add(searchField);
        add(new JLabel("in"));
        add(searchColumnComboBox);
        add(searchButton);
        add(clearButton);

        searchTimer = new Timer(300, e -> performSearch());
        searchTimer.setRepeats(false);
    }

    private void triggerDelayedSearch() {
        if (searchTimer.isRunning()) {
            searchTimer.stop();
        }
        searchTimer.start();
    }

    public void setSearchCallback(Consumer<String[]> searchCallback) {
        this.searchCallback = searchCallback;
    }

    private void performSearch() {
        if (searchCallback == null) {
            return;
        }

        String searchText = searchField.getText().trim();
        String columnName = (String) searchColumnComboBox.getSelectedItem();
        
        searchCallback.accept(new String[]{searchText, columnName});
    }

    public void setSearchText(String text) {
        searchField.setText(text);
    }

    public void setSearchColumn(String columnName) {
        for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
            if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                searchColumnComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    public void clearSearch() {
        searchField.setText("");
        searchColumnComboBox.setSelectedIndex(0);
        performSearch();
    }

    public void executeSearch() {
        performSearch();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public String getSelectedColumn() {
        return (String) searchColumnComboBox.getSelectedItem();
    }
}
}