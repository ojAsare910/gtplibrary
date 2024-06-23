package com.justiceasare.gtplibrary.controller;

import com.justiceasare.gtplibrary.dao.*;
import com.justiceasare.gtplibrary.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.sql.Timestamp;
import java.util.Optional;

public class AppController {

    @FXML private TableView<Book> bookTableView;
    @FXML private TableColumn<Book, String> bookTitleColumn;
    @FXML private TableColumn<Book, String> bookCategoryColumn;

    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, UserType> userTypeColumn;

    @FXML private TableView<Transaction> transactionTableView;
    @FXML private TableColumn<Transaction, Integer> transactionIdColumn;
    @FXML private TableColumn<Transaction, Integer> transactionUserIdColumn;
    @FXML private TableColumn<Transaction, Integer> copyIdColumn;
    @FXML private TableColumn<Transaction, String> transactionTypeColumn;
    @FXML private TableColumn<Transaction, Timestamp> transactionDateColumn;

    @FXML private TableView<FineUserDTO> fineUserTableView;
    @FXML private TableColumn<FineUserDTO, String> fineUserNameColumn;
    @FXML private TableColumn<FineUserDTO, String> fineBookTitleColumn;
    @FXML private TableColumn<FineUserDTO, Double> fineUserAmountColumn;
    @FXML private TableColumn<FineUserDTO, Boolean> fineUserPaidColumn;

    @FXML private TableView<Reservation> reservationTableView;
    @FXML private TableColumn<Reservation, Integer> reservationPatronIdColumn;
    @FXML private TableColumn<Reservation, Integer> reservationBookIdColumn;
    @FXML private TableColumn<Reservation, Timestamp> reservationDateColumn;

    private BookDAO bookDAO;
    private UserDAO userDAO;
    private TransactionDAO transactionDAO;
    private FineDAO fineDAO;
    private ReservationDAO reservationDAO;

    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
        transactionDAO = new TransactionDAO();
        fineDAO = new FineDAO();
        reservationDAO = new ReservationDAO();

        initializeBookTableView();
        initializeUserTableView();
        initializeTransactionTableView();
        initializeFineUserTableView();
        initializeReservationTableView();
    }

    private void initializeBookTableView() {
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        bookTableView.getItems().setAll(bookDAO.getAllBooks());
    }

    private void initializeUserTableView() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

        userTableView.getItems().setAll(userDAO.getAllUsers());
    }

    private void initializeTransactionTableView() {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        transactionUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        copyIdColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionTableView.getItems().setAll(transactionDAO.getAllTransactions());
    }

    private void initializeFineUserTableView() {
        fineUserNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fineBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        fineUserAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        fineUserPaidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));

        fineUserTableView.getItems().setAll(fineDAO.getAllFineWithUsername());
    }

    private void initializeReservationTableView() {
        reservationPatronIdColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));
        reservationBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));

        reservationTableView.getItems().setAll(reservationDAO.getAllReservations());
    }

    @FXML
    public GridPane createConfiguredGridPane() {
        // Create a new GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        return grid;
    }

    @FXML
    private void addBook() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add Book");
        dialog.setHeaderText("Enter Book Title and Category");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        GridPane grid = createConfiguredGridPane();

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || categoryField.getText().trim().isEmpty());
        });

        categoryField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = titleField.getText().trim();
                String category = categoryField.getText().trim();
                return new Book(0, title, category);
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            if (bookDAO.addBook(book)) {
                bookTableView.getItems().add(book);
            } else {
                showErrorAlert("Failed to add book");
            }
        });
    }

    @FXML
    private void updateBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Update Book");
            dialog.setHeaderText("Update Book Title and Category");

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            TextField titleField = new TextField();
            titleField.setText(selectedBook.getTitle());
            TextField categoryField = new TextField();
            categoryField.setText(selectedBook.getCategory());

            GridPane grid = createConfiguredGridPane();

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Category:"), 0, 1);
            grid.add(categoryField, 1, 1);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    return new Pair<>(titleField.getText(), categoryField.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(bookDetails -> {
                String newTitle = bookDetails.getKey();
                String newCategory = bookDetails.getValue();
                selectedBook.setTitle(newTitle);
                selectedBook.setCategory(newCategory);
                if (BookDAO.updateBook(selectedBook)) {
                    bookTableView.refresh();
                } else {
                    showErrorAlert("Failed to update book");
                }
            });
        } else {
            showErrorAlert("No book selected");
        }
    }

    @FXML
    private void addUser() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add User");
        dialog.setHeaderText("Enter Username, Email and Select User Type (PATRON or STAFF)");

        // Set the button types
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create the text fields and labels
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("PATRON", "STAFF");
        userTypeComboBox.setPromptText("User Type");

        GridPane grid = createConfiguredGridPane();
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("User Type:"), 0, 2);
        grid.add(userTypeComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Enable/Disable add button based on input validation
        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setDisable(true);

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButtonNode.setDisable(newValue.trim().isEmpty() || emailField.getText().trim().isEmpty() || userTypeComboBox.getValue() == null);
        });

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButtonNode.setDisable(newValue.trim().isEmpty() || usernameField.getText().trim().isEmpty() || userTypeComboBox.getValue() == null);
        });

        userTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            addButtonNode.setDisable(newValue == null || usernameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    String username = usernameField.getText().trim();
                    String email = emailField.getText().trim();
                    UserType userType = UserType.valueOf(userTypeComboBox.getValue().toUpperCase());

                    User newUser = new User(0, username, email, userType);
                    if (userDAO.addUser(newUser)) {
                        userTableView.getItems().add(newUser);
                        return true;
                    } else {
                        showErrorAlert("Failed to add user");
                    }
                } catch (IllegalArgumentException e) {
                    showErrorAlert("Invalid user type. Please enter PATRON or STAFF.");
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    @FXML
    private void updateUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Create a custom dialog
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("Update User");
            dialog.setHeaderText("Update Username, Email, and User Type");

            // Set the button types
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Create text fields for username, email, and user type
            TextField usernameField = new TextField(selectedUser.getUsername());
            TextField emailField = new TextField(selectedUser.getEmail());
            ComboBox<UserType> userTypeComboBox = new ComboBox<>();
            userTypeComboBox.getItems().addAll(UserType.values());
            userTypeComboBox.setValue(selectedUser.getUserType());

            // Layout setup
            GridPane grid = createConfiguredGridPane();
            grid.add(new Label("Username:"), 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(new Label("Email:"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("User Type:"), 0, 2);
            grid.add(userTypeComboBox, 1, 2);
            dialog.getDialogPane().setContent(grid);

            // Enable/Disable button based on input validation
            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            // Track changes in fields
            boolean[] fieldChanged = { false, false, false }; // username, email, userType

            // Validation logic for username field
            usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
                fieldChanged[0] = !newValue.trim().isEmpty() && !newValue.equals(selectedUser.getUsername());
                updateButton.setDisable(!fieldChanged[0] && !fieldChanged[1] && !fieldChanged[2]);
            });

            // Validation logic for email field
            emailField.textProperty().addListener((observable, oldValue, newValue) -> {
                fieldChanged[1] = !newValue.trim().isEmpty() && !newValue.equals(selectedUser.getEmail());
                updateButton.setDisable(!fieldChanged[0] && !fieldChanged[1] && !fieldChanged[2]);
            });

            // Validation logic for userType ComboBox
            userTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                fieldChanged[2] = !newValue.equals(selectedUser.getUserType());
                updateButton.setDisable(!fieldChanged[0] && !fieldChanged[1] && !fieldChanged[2]);
            });

            // Convert result to User object when update button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
                        if (fieldChanged[0]) {
                            selectedUser.setUsername(usernameField.getText().trim());
                        }
                        if (fieldChanged[1]) {
                            selectedUser.setEmail(emailField.getText().trim());
                        }
                        if (fieldChanged[2]) {
                            selectedUser.setUserType(userTypeComboBox.getValue());
                        }

                        if (userDAO.updateUser(selectedUser)) {
                            userTableView.refresh();
                            return true;
                        } else {
                            showErrorAlert("Failed to update user");
                        }
                    } catch (Exception e) {
                        showErrorAlert("Error occurred during update: " + e.getMessage());
                    }
                }
                return false;
            });

            dialog.showAndWait();
        } else {
            showErrorAlert("No user selected");
        }
    }

    @FXML
    private void addFine() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add Fine");
        dialog.setHeaderText("Select Patron and Enter Fine Amount");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        ObservableList<String> patronList = FXCollections.observableArrayList(FineDAO.getAllPatronNames());
        ComboBox<String> patronComboBox = new ComboBox<>(patronList);
        patronComboBox.setPromptText("Select Patron");

        Label userNameLabel = new Label();

        GridPane grid = createConfiguredGridPane();
        grid.add(new Label("Patron:"), 0, 0);
        grid.add(patronComboBox, 1, 0);
        grid.add(new Label("User Name:"), 0, 1);
        grid.add(userNameLabel, 1, 1);
        grid.add(new Label("Amount:"), 0, 2);
        grid.add(amountField, 1, 2);
        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        patronComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int userId = FineDAO.getUserId(newValue); // Fetch user ID from DAO
                if (userId != -1) {
                    String userName = FineDAO.getUserName(userId); // Fetch user name from DAO
                    if (userName != null) {
                        userNameLabel.setText(userName);
                        addButton.setDisable(false);
                    } else {
                        userNameLabel.setText("User not found");
                        addButton.setDisable(true);
                    }
                } else {
                    userNameLabel.setText("User not found");
                    addButton.setDisable(true);
                }
            } else {
                userNameLabel.setText("");
                addButton.setDisable(true);
            }
        });

        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("\\d+(\\.\\d+)?"));
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String selectedPatron = patronComboBox.getValue();
                    double amount = Double.parseDouble(amountField.getText().trim());

                    int userId = FineDAO.getUserId(selectedPatron);

                    Fine newFine = new Fine(1, userId, amount, false);
                    if (FineDAO.addFine(newFine)) {
//                        fineUserTableView.refresh();
                        fineUserTableView.getItems().getClass();
                    } else {
                        showErrorAlert("Failed to add fine");
                    }
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input format. Amount must be a number.");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    @FXML
    private void updateFine() {
        FineUserDTO selectedFine = fineUserTableView.getSelectionModel().getSelectedItem();
        if (selectedFine != null) {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("Update Fine");
            dialog.setHeaderText("Update Transaction ID, Amount, and Paid");

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            TextField transactionIdField = new TextField(String.valueOf(selectedFine.getTransactionId()));
            TextField amountField = new TextField(String.valueOf(selectedFine.getAmount()));
            CheckBox paidCheckBox = new CheckBox("Paid");
            paidCheckBox.setSelected(selectedFine.isPaid());

            GridPane grid = createConfiguredGridPane();

            grid.add(new Label("Transaction ID:"), 0, 0);
            grid.add(transactionIdField, 1, 0);
            grid.add(new Label("Amount:"), 0, 1);
            grid.add(amountField, 1, 1);
            grid.add(paidCheckBox, 0, 2, 2, 1);
            dialog.getDialogPane().setContent(grid);

            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            transactionIdField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("\\d+"));
            });
            amountField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("\\d+(\\.\\d+)?"));
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
                        int transactionId = Integer.parseInt(transactionIdField.getText().trim());
                        double amount = Double.parseDouble(amountField.getText().trim());
                        boolean paid = paidCheckBox.isSelected();
                        selectedFine.setTransactionId(transactionId);
                        selectedFine.setAmount(amount);
                        selectedFine.setPaid(paid);
                        if (fineDAO.updateFine(selectedFine)) {
                            fineUserTableView.refresh();
                        } else {
                            showErrorAlert("Failed to update fine");
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Invalid input format. Transaction ID must be an integer and Amount must be a number.");
                    }
                }
                return false;
            });

            dialog.showAndWait();
        } else {
            showErrorAlert("No fine selected");
        }
    }

    @FXML
    private void addReservation() {

        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add Reservation");
        dialog.setHeaderText("Enter Patron ID and Book ID");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField patronIdField = new TextField();
        patronIdField.setPromptText("Patron ID");

        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");

        GridPane grid = createConfiguredGridPane();
        grid.add(new Label("Patron ID:"), 0, 0);
        grid.add(patronIdField, 1, 0);
        grid.add(new Label("Book ID:"), 0, 1);
        grid.add(bookIdField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        patronIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("\\d+"));
        });

        bookIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("\\d+"));
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int patronId = Integer.parseInt(patronIdField.getText().trim());
                    int bookId = Integer.parseInt(bookIdField.getText().trim());
                    Timestamp reservationDate = new Timestamp(System.currentTimeMillis()); // Assuming current time

                    Reservation newReservation = new Reservation(0, patronId, bookId, reservationDate);
                    if (reservationDAO.addReservation(newReservation)) {
                        reservationTableView.getItems().add(newReservation);
                        return true;
                    } else {
                        showErrorAlert("Failed to add reservation");
                    }
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input format. Patron ID and Book ID must be integers.");
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    @FXML
    private void updateReservation() {
        Reservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("Update Reservation");
            dialog.setHeaderText("Update Patron ID and/or Book ID");

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            TextField patronIdField = new TextField(String.valueOf(selectedReservation.getPatronId()));
            TextField bookIdField = new TextField(String.valueOf(selectedReservation.getBookId()));

            GridPane grid = createConfiguredGridPane();
            grid.add(new Label("Patron ID:"), 0, 0);
            grid.add(patronIdField, 1, 0);
            grid.add(new Label("Book ID:"), 0, 1);
            grid.add(bookIdField, 1, 1);
            dialog.getDialogPane().setContent(grid);

            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            patronIdField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("\\d+"));
            });

            bookIdField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("\\d+"));
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
                        int patronId = patronIdField.getText().trim().isEmpty() ? selectedReservation.getPatronId() : Integer.parseInt(patronIdField.getText().trim());
                        int bookId = bookIdField.getText().trim().isEmpty() ? selectedReservation.getBookId() : Integer.parseInt(bookIdField.getText().trim());

                        // Check if the patronId exists in the patron table
                        if (!isPatronExists(patronId)) {
                            showErrorAlert("Patron with ID " + patronId + " does not exist.");
                            return false;
                        }

                        selectedReservation.setPatronId(patronId);
                        selectedReservation.setBookId(bookId);

                        if (reservationDAO.updateReservation(selectedReservation)) {
                            reservationTableView.refresh();
                            return true;
                        } else {
                            showErrorAlert("Failed to update reservation");
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Invalid input format. Patron ID and Book ID must be integers.");
                    }
                }
                return false;
            });

            dialog.showAndWait();
        } else {
            showErrorAlert("No reservation selected");
        }
    }

    private boolean isPatronExists(int patronId) {
        // Implement DAO method to check if patronId exists in patron table
        return reservationDAO.existsPatron(patronId);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
