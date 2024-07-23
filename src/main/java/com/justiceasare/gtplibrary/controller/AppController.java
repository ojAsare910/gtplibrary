package com.justiceasare.gtplibrary.controller;

import com.justiceasare.gtplibrary.dao.*;
import com.justiceasare.gtplibrary.model.*;
import javafx.beans.value.ChangeListener;
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
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class AppController {

    @FXML private Tab bookTab;
    @FXML private Tab userTab;
    @FXML private Tab reservationTab;
    @FXML private Tab transactionTab;

    @FXML private TableView<Book> bookTableView;
    @FXML private TableColumn<Book, String> bookTitleColumn;
    @FXML private TableColumn<Book, String> bookAuthorColumn;
    @FXML private TableColumn<Book, String> bookIsbnColumn;
    @FXML private TableColumn<Book, String> bookCategoryColumn;

    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, UserType> userTypeColumn;

    @FXML private TableView<Reservation> reservationTableView;
    @FXML private TableColumn<Reservation, String> reservationUsernameColumn;
    @FXML private TableColumn<Reservation, String> reservationBookTitleColumn;
    @FXML private TableColumn<Reservation, ReservationType> reservationTypeColumn;
    @FXML private TableColumn<Reservation, Date> reservationDateColumn;

    @FXML private TableView<TransactionHistory> transactionTableView;
    @FXML private TableColumn<TransactionHistory, String> transactionBookTitleColumn;
    @FXML private TableColumn<TransactionHistory, String> transactionUsernameColumn;
    @FXML private TableColumn<TransactionHistory, String> transactionTypeColumn;
    @FXML private TableColumn<TransactionHistory, Timestamp> transactionDateColumn;

    private BookDAO bookDAO;
    private UserDAO userDAO;
    private TransactionDAO transactionDAO;
    private ReservationDAO reservationDAO;
    private ObservableList<Reservation> reservationData = FXCollections.observableArrayList();
    private ObservableList<TransactionHistory> transactionData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
        transactionDAO = new TransactionDAO();
        reservationDAO = new ReservationDAO();

        initializeBookTableView();
        initializeUserTableView();
        initializeTransactionTableView();
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

    @FXML
    private void handleBookTabSelected() {
        if (bookTab.isSelected()) {
            bookTableView.getItems().setAll(BookDAO.getAllBooks());
        }
    }

    @FXML
    private void handleUserTabSelected() {
        if (userTab.isSelected()) {
            userTableView.getItems().setAll(userDAO.getAllUsers());
        }
    }

    @FXML
    private void handleReservationTabSelected() {
        if (reservationTab.isSelected()) {
            reservationTableView.getItems().setAll(reservationDAO.getAllReservations());
        }
    }

    @FXML
    private void handleTransactionTabSelected() {
        if (transactionTab.isSelected()) {
            transactionTableView.getItems().setAll(transactionDAO.getAllTransactions());
        }
    }

    private void initializeBookTableView() {
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        bookTableView.setItems(FXCollections.observableArrayList(BookDAO.getAllBooks()));
        bookTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeUserTableView() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

        userTableView.getItems().setAll(userDAO.getAllUsers());
        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeTransactionTableView() {
        transactionBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        transactionUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reservationType"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionTableView.setItems(transactionData);
        transactionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    private void initializeReservationTableView() {
        reservationBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        reservationUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        reservationTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reservationType"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        reservationTableView.getItems().setAll(reservationDAO.getAllReservations());
        reservationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

        // Enable/disable add button based on text field changes
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || authorField.getText().trim().isEmpty()
                    || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty()
                    || BookDAO.bookExists(newValue.trim(), isbnField.getText().trim()));
        });

        authorField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                    || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty()
                    || BookDAO.bookExists(titleField.getText().trim(), isbnField.getText().trim()));
        });

        isbnField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                    || authorField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty()
                    || BookDAO.bookExists(titleField.getText().trim(), newValue.trim()));
        });

        categoryField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                    || authorField.getText().trim().isEmpty() || isbnField.getText().trim().isEmpty()
                    || BookDAO.bookExists(titleField.getText().trim(), isbnField.getText().trim()));
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();
                String category = categoryField.getText().trim();
                return new Book(0, title, author, isbn, category, false);
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            int bookId = BookDAO.addBook(book);
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

            // Enable/disable update button based on text field changes
            titleField.textProperty().addListener((observable, oldValue, newValue) ->
                    updateButton.setDisable(newValue.trim().isEmpty() || authorField.getText().trim().isEmpty()
                            || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty()
                            || newValue.equals(selectedBook.getTitle()) && authorField.getText().equals(selectedBook.getAuthor())
                            && isbnField.getText().equals(selectedBook.getIsbn()) && categoryField.getText().equals(selectedBook.getCategory())
                            || BookDAO.bookExists(newValue.trim(), isbnField.getText().trim())
                    ));

            authorField.textProperty().addListener((observable, oldValue, newValue) ->
                    updateButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                            || isbnField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty()
                            || titleField.getText().equals(selectedBook.getTitle()) && newValue.equals(selectedBook.getAuthor())
                            && isbnField.getText().equals(selectedBook.getIsbn()) && categoryField.getText().equals(selectedBook.getCategory())
                            || BookDAO.bookExists(titleField.getText().trim(), isbnField.getText().trim())
                    ));

            isbnField.textProperty().addListener((observable, oldValue, newValue) ->
                    updateButton.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty()
                            || authorField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty()
                            || titleField.getText().equals(selectedBook.getTitle()) && authorField.getText().equals(selectedBook.getAuthor())
                            && newValue.equals(selectedBook.getIsbn()) && categoryField.getText().equals(selectedBook.getCategory())
                            || BookDAO.bookExists(titleField.getText().trim(), newValue.trim())
                    ));

            categoryField.textProperty().addListener((observable, oldValue, newValue) -> {
                boolean isCategoryChanged = !newValue.equals(selectedBook.getCategory());
                boolean areOtherFieldsUnchanged = titleField.getText().equals(selectedBook.getTitle())
                        && authorField.getText().equals(selectedBook.getAuthor())
                        && isbnField.getText().equals(selectedBook.getIsbn());
                updateButton.setDisable(newValue.trim().isEmpty() || (isCategoryChanged && areOtherFieldsUnchanged)
                        || BookDAO.bookExists(titleField.getText().trim(), isbnField.getText().trim())
                );
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
    private void deleteBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Book");
            alert.setHeaderText("Are you sure you want to delete the selected book?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (BookDAO.deleteBook(selectedBook.getBookId())) {
                    bookTableView.getItems().remove(selectedBook);
                    bookTableView.refresh();
                } else {
                    showErrorAlert("Failed to delete book");
                }
            }
        } else {
            showErrorAlert("No book selected");
        }
    }

    @FXML
    private void addUser() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add User");
        dialog.setHeaderText("Enter Name, Email and Select User Type (PATRON or STAFF)");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

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

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Enable/disable add button based on text field changes
        ChangeListener<String> fieldChangeListener = (observable, oldValue, newValue) -> {
            addButton.setDisable(usernameField.getText().trim().isEmpty()
                    || emailField.getText().trim().isEmpty()
                    || !isValidEmail(emailField.getText().trim())
                    || userTypeComboBox.getValue() == null
                    || userDAO.userExists(usernameField.getText().trim(), emailField.getText().trim()));
        };

        usernameField.textProperty().addListener(fieldChangeListener);
        emailField.textProperty().addListener(fieldChangeListener);

        userTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(usernameField.getText().trim().isEmpty()
                    || emailField.getText().trim().isEmpty()
                    || !isValidEmail(emailField.getText().trim())
                    || userTypeComboBox.getValue() == null
                    || userDAO.userExists(usernameField.getText().trim(), emailField.getText().trim()));
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String username = usernameField.getText().trim();
                    String email = emailField.getText().trim();
                    UserType userType = UserType.valueOf(userTypeComboBox.getValue().toUpperCase());

                    User newUser = new User(0, username, email, userType);
                    int userId = userDAO.addUser(newUser);

                    if (userId == -2) {
                        showErrorAlert("User already exists");
                    } else if (userId > 0) {
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

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    @FXML
    private void updateUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("Update User");
            dialog.setHeaderText("Update Name, Email, and User Type");

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
    private void addReservation() {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Add Reservation");
        dialog.setHeaderText("Enter Reservation Details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Fetch usernames and book titles
        List<String> usernames = ReservationDAO.getAllUsernames();
        List<String> bookTitles = ReservationDAO.getAllBookTitles();

        ComboBox<String> usernameComboBox = new ComboBox<>();
        usernameComboBox.getItems().addAll(usernames);
        usernameComboBox.setPromptText("Select Username");

        ComboBox<String> bookTitleComboBox = new ComboBox<>();
        bookTitleComboBox.getItems().addAll(bookTitles);
        bookTitleComboBox.setPromptText("Select Book Title");

        ComboBox<ReservationType> reservationTypeComboBox = new ComboBox<>();
        reservationTypeComboBox.getItems().setAll(ReservationType.values());
        reservationTypeComboBox.setPromptText("Reservation Type");

        DatePicker reservationDatePicker = new DatePicker();
        reservationDatePicker.setPromptText("Reservation Date");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameComboBox, 1, 0);
        grid.add(new Label("Book Title:"), 0, 1);
        grid.add(bookTitleComboBox, 1, 1);
        grid.add(new Label("Reservation Type:"), 0, 2);
        grid.add(reservationTypeComboBox, 1, 2);
        grid.add(new Label("Reservation Date:"), 0, 3);
        grid.add(reservationDatePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Enable the add button only when all fields are filled and date is not in the past
        usernameComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton, usernameComboBox, bookTitleComboBox, reservationTypeComboBox, reservationDatePicker));
        bookTitleComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton, usernameComboBox, bookTitleComboBox, reservationTypeComboBox, reservationDatePicker));
        reservationTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton, usernameComboBox, bookTitleComboBox, reservationTypeComboBox, reservationDatePicker));
        reservationDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateInput(addButton, usernameComboBox, bookTitleComboBox, reservationTypeComboBox, reservationDatePicker));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String username = usernameComboBox.getValue();
                String bookTitle = bookTitleComboBox.getValue();
                ReservationType reservationType = reservationTypeComboBox.getValue();
                LocalDate reservationDate = reservationDatePicker.getValue();

                // Check if the selected date is in the past
                if (reservationDate.isBefore(LocalDate.now())) {
                    showErrorAlert("Reservation date cannot be in the past.");
                    return null;
                }

                // Check if the book and user exist and retrieve their IDs
                Integer bookId = ReservationDAO.getBookIdByTitle(bookTitle);
                Integer userId = ReservationDAO.getUserIdByUsername(username);

                if (bookId == null) {
                    showErrorAlert("Book title does not exist.");
                    return null;
                }
                if (userId == null) {
                    showErrorAlert("Username does not exist.");
                    return null;
                }

                Reservation newReservation = new Reservation(0, bookTitle, username, reservationType, reservationDate, false);
                if (ReservationDAO.addReservation(newReservation) > 0) {
                    reservationData.add(newReservation);
                    reservationTableView.refresh();
                    return newReservation;
                } else {
                    showErrorAlert("Failed to add reservation.");
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

            TextField bookTitleField = new TextField(selectedReservation.getBookTitle());
            bookTitleField.setEditable(false);

            TextField usernameField = new TextField(selectedReservation.getUsername());
            usernameField.setEditable(false);

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
            grid.add(new Label("Book Title:"), 0, 0);
            grid.add(bookTitleField, 1, 0);
            grid.add(new Label("Username:"), 0, 1);
            grid.add(usernameField, 1, 1);
            grid.add(new Label("Reservation Type:"), 0, 2);
            grid.add(reservationTypeComboBox, 1, 2);
            grid.add(new Label("Reservation Date:"), 0, 3);
            grid.add(reservationDatePicker, 1, 3);

            dialog.getDialogPane().setContent(grid);

            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            reservationTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue == null || reservationDatePicker.getValue() == null || reservationDatePicker.getValue().isBefore(LocalDate.now()));
            });

            reservationDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateButton.setDisable(newValue == null || reservationTypeComboBox.getValue() == null || newValue.isBefore(LocalDate.now()));
            });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    ReservationType newReservationType = reservationTypeComboBox.getValue();
                    LocalDate newReservationDate = reservationDatePicker.getValue();

                    // Check if the selected date is in the past
                    if (newReservationDate.isBefore(LocalDate.now())) {
                        showErrorAlert("Reservation date cannot be in the past.");
                        return null;
                    }

                    selectedReservation.setReservationType(newReservationType);
                    selectedReservation.setReservationDate(newReservationDate);

                    if (ReservationDAO.updateReservation(selectedReservation)) {
                        reservationTableView.refresh();
                        return selectedReservation;
                    } else {
                        showErrorAlert("Failed to update reservation.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } else {
            showErrorAlert("No reservation selected.");
        }
    }

    private void validateInput(Node addButton, ComboBox<String> usernameComboBox, ComboBox<String> bookTitleComboBox,
                               ComboBox<ReservationType> reservationTypeComboBox, DatePicker reservationDatePicker) {
        // Check if any of the required fields are empty or the date is in the past
        boolean disable = usernameComboBox.getValue() == null ||
                bookTitleComboBox.getValue() == null ||
                reservationTypeComboBox.getValue() == null ||
                reservationDatePicker.getValue() == null ||
                reservationDatePicker.getValue().isBefore(LocalDate.now());

        addButton.setDisable(disable);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
