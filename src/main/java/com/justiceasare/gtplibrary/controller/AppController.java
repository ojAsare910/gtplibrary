package com.justiceasare.gtplibrary.controller;

import com.justiceasare.gtplibrary.dao.*;
import com.justiceasare.gtplibrary.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class AppController {

    @FXML private TableView<Book> bookTableView;
    @FXML private TableColumn<Book, String> bookTitleColumn;
    @FXML private TableColumn<Book, String> bookAuthorColumn;
    @FXML private TableColumn<Book, String> bookIsbnColumn;
    @FXML private TableColumn<Book, String> bookCategoryColumn;
    @FXML private TableColumn<Book, String> statusColumn;

    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, UserType> userTypeColumn;

    @FXML private TableView<TransactionHistory> transactionTableView;
    @FXML private TableColumn<TransactionHistory, Integer> transactionIdColumn;
    @FXML private TableColumn<TransactionHistory, Integer> transactionBookIdColumn;
    @FXML private TableColumn<TransactionHistory, Integer> transactionUserIdColumn;
    @FXML private TableColumn<TransactionHistory, String> transactionTypeColumn;
    @FXML private TableColumn<TransactionHistory, Timestamp> transactionDateColumn;

    @FXML private TableView<FineUserDTO> fineUserTableView;
    @FXML private TableColumn<FineUserDTO, String> fineUserNameColumn;
    @FXML private TableColumn<FineUserDTO, String> fineBookTitleColumn;
    @FXML private TableColumn<FineUserDTO, Double> fineUserAmountColumn;
    @FXML private TableColumn<FineUserDTO, Boolean> fineUserPaidColumn;

    @FXML private TableView<Reservation> reservationTableView;
    @FXML private TableColumn<Reservation, Integer> reservationUserIdColumn;
    @FXML private TableColumn<Reservation, Integer> reservationBookIdColumn;
    @FXML private TableColumn<Reservation, ReservationType> reservationTypeColumn;
    @FXML private TableColumn<Reservation, Boolean> reservationIsCompletedColumn;
    @FXML private TableColumn<Reservation, Date> reservationDateColumn;

    private BookDAO bookDAO;
    private UserDAO userDAO;
    private TransactionDAO transactionDAO;
    private FineDAO fineDAO;
    private ReservationDAO reservationDAO;
    private ObservableList<FineUserDTO> fineData = FXCollections.observableArrayList();
    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();
    private ObservableList<TransactionHistory> transactionData = FXCollections.observableArrayList();

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
        loadReservationData();
        loadTransactionData();
    }

    private void loadReservationData() {
        reservationData.clear();
        reservationData.addAll(reservationDAO.getAllReservations());
        reservationTableView.setItems(reservationData);
    }

    private void loadTransactionData() {
        transactionData.clear();
        transactionData.addAll(transactionDAO.getAllTransactions());
        transactionTableView.setItems(transactionData);
    }

    private void initializeBookTableView() {
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            if (!book.getCopies().isEmpty()) {
                return new SimpleStringProperty(book.getCopies().get(0).getStatus().name());
            } else {
                return new SimpleStringProperty("AVAILABLE");
            }
        });
        bookTableView.setItems(FXCollections.observableArrayList(BookDAO.getAllBooks()));
    }

    private void initializeUserTableView() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

        userTableView.getItems().setAll(userDAO.getAllUsers());
    }

    private void initializeTransactionTableView() {
        transactionBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        transactionUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reservationType"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionTableView.setItems(transactionData);
    }

    private void initializeFineUserTableView() {
        fineUserNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fineBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        fineUserAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        fineUserPaidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));

        fineUserTableView.getItems().setAll(FineDAO.getAllFineWithUsername());
    }

    private void initializeReservationTableView() {
        reservationBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        reservationUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        reservationTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reservationType"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        reservationIsCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));

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
        dialog.setHeaderText("Enter Book Title, Author, ISBN, and Category");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        GridPane grid = createConfiguredGridPane();

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || authorField.getText().trim().isEmpty()
                    || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty());
        });

        authorField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                    || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty());
        });

        isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                    || authorField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty());
        });

        categoryField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                    || authorField.getText().trim().isEmpty() || isbnField.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();
                String category = categoryField.getText().trim();
                return new Book(0, title, author, isbn, category);
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            int bookId = bookDAO.addBook(book);
            if (bookId > 0) {
                book.setBookId(bookId);
                bookTableView.getItems().add(book);
                bookTableView.refresh();
            } else {
                showErrorAlert("Failed to add book");
            }
        });
    }

    @FXML
    private void updateBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Dialog<Book> dialog = new Dialog<>();
            dialog.setTitle("Update Book");
            dialog.setHeaderText("Update Book Title, Author, ISBN, and Category");

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            TextField titleField = new TextField();
            titleField.setText(selectedBook.getTitle());

            TextField authorField = new TextField();
            authorField.setText(selectedBook.getAuthor());

            TextField isbnField = new TextField();
            isbnField.setText(selectedBook.getIsbn());

            TextField categoryField = new TextField();
            categoryField.setText(selectedBook.getCategory());

            GridPane grid = createConfiguredGridPane();

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Author:"), 0, 1);
            grid.add(authorField, 1, 1);
            grid.add(new Label("ISBN:"), 0, 2);
            grid.add(isbnField, 1, 2);
            grid.add(new Label("Category:"), 0, 3);
            grid.add(categoryField, 1, 3);

            dialog.getDialogPane().setContent(grid);

            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            titleField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || authorField.getText().trim().isEmpty()
                        || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty());
            });

            authorField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                        || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty());
            });

            isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                        || authorField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty());
            });

            categoryField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                        || authorField.getText().trim().isEmpty() || isbnField.getText().trim().isEmpty());
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    String newTitle = titleField.getText().trim();
                    String newAuthor = authorField.getText().trim();
                    String newIsbn = isbnField.getText().trim();
                    String newCategory = categoryField.getText().trim();
                    selectedBook.setTitle(newTitle);
                    selectedBook.setAuthor(newAuthor);
                    selectedBook.setIsbn(newIsbn);
                    selectedBook.setCategory(newCategory);
                    return selectedBook;
                }
                return null;
            });

            Optional<Book> result = dialog.showAndWait();
            result.ifPresent(updatedBook -> {
                if (BookDAO.updateBook(updatedBook)) {
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
        Dialog<User> dialog = new Dialog<>();
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
                    int userId = userDAO.addUser(newUser);
                    if (userId > 0) {
                        newUser.setUserId(userId);
                        return newUser;
                    } else {
                        showErrorAlert("Failed to add user");
                    }
                } catch (IllegalArgumentException e) {
                    showErrorAlert("Invalid user type. Please enter PATRON or STAFF.");
                }
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            userTableView.getItems().add(user);
        });
    }

    @FXML
    private void updateUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("Update User");
            dialog.setHeaderText("Update Username, Email, and User Type");

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

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

            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            boolean[] fieldChanged = { false, false, false };

            usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
                fieldChanged[0] = !newValue.trim().isEmpty() && !newValue.equals(selectedUser.getUsername());
                updateButton.setDisable(!fieldChanged[0] && !fieldChanged[1] && !fieldChanged[2]);
            });

            emailField.textProperty().addListener((observable, oldValue, newValue) -> {
                fieldChanged[1] = !newValue.trim().isEmpty() && !newValue.equals(selectedUser.getEmail());
                updateButton.setDisable(!fieldChanged[0] && !fieldChanged[1] && !fieldChanged[2]);
            });

            userTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                fieldChanged[2] = !newValue.equals(selectedUser.getUserType());
                updateButton.setDisable(!fieldChanged[0] && !fieldChanged[1] && !fieldChanged[2]);
            });

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
                int userId = FineDAO.getUserId(newValue);
                if (userId != -1) {
                    String userName = FineDAO.getUserName(userId);
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
                        fineData.clear();
                        fineData.addAll(FineDAO.getAllFineWithUsername());
                        fineUserTableView.setItems(fineData);
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
        int userId = selectedFine.getUserId();
        String userName = FineDAO.getUserName(userId);
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

            Label userNameLabel = new Label(selectedFine.getUsername());

            GridPane grid = createConfiguredGridPane();
            grid.add(new Label("Transaction ID:"), 0, 0);
            grid.add(transactionIdField, 1, 0);
            grid.add(new Label("Amount:"), 0, 1);
            grid.add(amountField, 1, 1);
            grid.add(new Label("User Name:"), 0, 2);
            grid.add(userNameLabel, 1, 2);
            grid.add(paidCheckBox, 0, 3, 2, 1);
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

                        // Update all fields in selectedFine
                        selectedFine.setTransactionId(transactionId);
                        selectedFine.setAmount(amount);
                        selectedFine.setPaid(paid);

                        // Update using user ID (assuming FineUserDTO has userId field)
                        if (fineDAO.updateFine(selectedFine)) {
                            fineUserTableView.refresh();
                        } else {
                            showErrorAlert("Failed to update fine");
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Invalid input format. Transaction ID must be an integer and Amount must be a number.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } else {
            showErrorAlert("No fine selected");
        }
    }

    @FXML
    private void addReservation() {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Add Reservation");
        dialog.setHeaderText("Enter Reservation Details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");

        ComboBox<ReservationType> reservationTypeComboBox = new ComboBox<>();
        reservationTypeComboBox.getItems().setAll(ReservationType.values());
        reservationTypeComboBox.setPromptText("Reservation Type");

        DatePicker reservationDatePicker = new DatePicker();
        reservationDatePicker.setPromptText("Reservation Date");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Book ID:"), 0, 0);
        grid.add(bookIdField, 1, 0);
        grid.add(new Label("User ID:"), 0, 1);
        grid.add(userIdField, 1, 1);
        grid.add(new Label("Reservation Type:"), 0, 2);
        grid.add(reservationTypeComboBox, 1, 2);
        grid.add(new Label("Reservation Date:"), 0, 3);
        grid.add(reservationDatePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        bookIdField.textProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton));
        userIdField.textProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton));
        reservationTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton));
        reservationDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int transactionId = Integer.parseInt(bookIdField.getText().trim());
                    int bookId = Integer.parseInt(bookIdField.getText().trim());
                    int userId = Integer.parseInt(userIdField.getText().trim());
                    ReservationType reservationType = reservationTypeComboBox.getValue();
                    LocalDate reservationDate = reservationDatePicker.getValue();

                    Reservation newReservation = new Reservation(transactionId, bookId, userId, reservationType, reservationDate, false);
                    if (ReservationDAO.addReservation(newReservation)) {
                        reservationData.add(newReservation);

                        TransactionHistory newTransactionHistory = new TransactionHistory(transactionId, bookId, userId, reservationType, reservationDate);
                        System.out.println(transactionId);
                        transactionData.add(newTransactionHistory);

                        reservationTableView.refresh();
                        transactionTableView.refresh();
                        return newReservation;
                    } else {
                        showErrorAlert("Failed to add reservation.");
                    }
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input. Please enter valid numbers for Book ID and User ID.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void updateReservation() {
        Reservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            Dialog<Reservation> dialog = new Dialog<>();
            dialog.setTitle("Update Reservation");
            dialog.setHeaderText("Update Reservation Details");

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            TextField bookIdField = new TextField(String.valueOf(selectedReservation.getBookId()));
            bookIdField.setEditable(false);

            TextField userIdField = new TextField(String.valueOf(selectedReservation.getUserId()));
            userIdField.setEditable(false);

            // Populate reservationTypes with enum values
            ObservableList<ReservationType> reservationTypes = FXCollections.observableArrayList(ReservationType.values());
            ComboBox<ReservationType> reservationTypeComboBox = new ComboBox<>(reservationTypes);
            reservationTypeComboBox.setValue(selectedReservation.getReservationType());
            reservationTypeComboBox.setPromptText("Select Reservation Type");

            DatePicker reservationDatePicker = new DatePicker(selectedReservation.getReservationDate());
            reservationDatePicker.setPromptText("Reservation Date");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(new Label("Book ID:"), 0, 0);
            grid.add(bookIdField, 1, 0);
            grid.add(new Label("User ID:"), 0, 1);
            grid.add(userIdField, 1, 1);
            grid.add(new Label("Reservation Type:"), 0, 2);
            grid.add(reservationTypeComboBox, 1, 2);
            grid.add(new Label("Reservation Date:"), 0, 3);
            grid.add(reservationDatePicker, 1, 3);

            dialog.getDialogPane().setContent(grid);

            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            reservationTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue == null || reservationDatePicker.getValue() == null);
            });

            reservationDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue == null || reservationTypeComboBox.getValue() == null);
            });

            dialog.setResultConverter(dialogButton -> {
                ReservationType newReservationType = reservationTypeComboBox.getValue();
                LocalDate newReservationDate = reservationDatePicker.getValue();

                TransactionHistory transaction = new TransactionHistory(
                        0,
                        selectedReservation.getBookId(),
                        selectedReservation.getUserId(),
                        selectedReservation.getReservationType(),
                        LocalDate.now()
                );

                if (transactionDAO.addTransaction(transaction)) {
                    selectedReservation.setReservationType(newReservationType);
                    selectedReservation.setReservationDate(newReservationDate);
                    if (reservationDAO.updateReservation(selectedReservation)) {
                        reservationTableView.refresh();
                        transactionTableView.refresh();
                        return selectedReservation;
                    }
                }
                return null;
            });

            dialog.showAndWait();
        }
    }

    private boolean isPatronExists(int patronId) {
        return reservationDAO.existsPatron(patronId);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void validateInput(Node addButton) {
        addButton.setDisable(false);
    }
}
