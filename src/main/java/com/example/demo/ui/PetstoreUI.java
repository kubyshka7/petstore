package com.example.demo.ui;

import com.example.demo.orders.OrderRequest;
import com.example.demo.orders.OrderResponse;
import com.example.demo.orders.Status;
import com.example.demo.pets.PetResponse;
import com.example.demo.pets.request.PetRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PetstoreUI {
    private static final String NAME_REGEX = "^[A-za-zА-Яа-яЁё]+$";
    private static final Color colourBlue = new Color(31, 41, 55);
    private static final Color colourGrey = new Color(220, 220, 220);
    private static final String PETS_URL = "http://localhost:8080/api/pet";
    private static final String ORDERS_URL = "http://localhost:8080/api/order";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gsonTime = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    private static final Gson gsonStatus = new GsonBuilder().registerTypeAdapter(Status.class, new StatusAdapter()).create();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PetstoreUI::createUI);
    }

    public static boolean isValidTime(String input){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            LocalDate.parse(input, formatter);
            return true;
        } catch (DateTimeParseException ex){
            return false;
        }
    }

    private static void styleMenuButton(JButton button){
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(colourBlue);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
    }

    private static void styleMenuText(JLabel label){
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        label.setBackground(colourBlue);
        label.setForeground(Color.WHITE);
    }

    private static void styleContentButton(JButton button){
        button.setSize(100, 100);
        button.setPreferredSize(new Dimension(100, 100));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(colourBlue);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
    }

    private static void styleBigContentButton(JButton button){
        button.setPreferredSize(new Dimension(100, 100));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(colourBlue);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
    }

    private static void styleContentText(JLabel label){
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBackground(Color.WHITE);
        label.setForeground(colourBlue);
    }

    private static void setLayout(JPanel panel, GridBagConstraints gridBagConstraints){
        panel.setLayout(new GridBagLayout());
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
    }

    private static void fillBtm(JPanel panel){
        GridBagConstraints gbcBtm = new GridBagConstraints();
        gbcBtm.gridy = 8;
        gbcBtm.gridx = 0;
        gbcBtm.weighty = 1.0;
        gbcBtm.fill = GridBagConstraints.BOTH;
        panel.add(new JPanel(), gbcBtm);
    }

    private static void styleTable(JTable table){
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(colourGrey);

        Color textColor = colourBlue;

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        cellRenderer.setForeground(textColor);
        cellRenderer.setBackground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        headerRenderer.setForeground(textColor);
        headerRenderer.setBackground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setRowHeight(32);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private static void styleSearchBtn(JButton button){
        button.setSize(100, 30);
        button.setPreferredSize(new Dimension(100, 30));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(colourBlue);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
    }

    public static List<PetResponse> getAllPets() throws IOException {
        Request request = new Request.Builder().url(PETS_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
            String json = response.body().string();
            Type listType = new TypeToken<List<PetResponse>>(){}.getType();
            return gsonTime.fromJson(json, listType);
        }
    }

    private static PetResponse getPet(String id) throws IOException {
        String urlID = PETS_URL + "/" + id;
        Request request = new Request.Builder().url(urlID).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
            String json = response.body().string();
            PetResponse pet = gsonTime.fromJson(json, PetResponse.class);
            return pet;
        }
    }

    private static void createPet(PetRequest petRequest) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        String json = gson.toJson(petRequest);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(PETS_URL).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
        }
    }

    private static void changePet(Long id, PetRequest petRequest) throws IOException{
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        String json = gson.toJson(petRequest);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(PETS_URL + "/" + id).put(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
        }
    }

    private static void deletePet(Long id) throws IOException{
        String urlID = PETS_URL + "/" + id;
        Request request = new Request.Builder().url(urlID).delete().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
        }
    }

    private static List<OrderResponse> getAllOrders() throws IOException {
        Request request = new Request.Builder().url(ORDERS_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
            String json = response.body().string();
            Type listType = new TypeToken<List<OrderResponse>>(){}.getType();
            return gsonTime.fromJson(json, listType);
        }
    }

    private static OrderResponse getOrder(String id) throws IOException {
        String urlID = ORDERS_URL + "/" + id;
        Request request = new Request.Builder().url(urlID).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
            String json = response.body().string();
            OrderResponse order = gsonTime.fromJson(json, OrderResponse.class);
            return order;
        }
    }

    private static void createOrder(OrderRequest orderRequest) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).registerTypeAdapter(Status.class, new StatusAdapter()).create();
        String json = gson.toJson(orderRequest);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(ORDERS_URL).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
        }
    }

    private static void changeOrder(Long id, OrderRequest orderRequest) throws IOException{
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).registerTypeAdapter(Status.class, new StatusAdapter()).create();
        String json = gson.toJson(orderRequest);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(ORDERS_URL + "/" + id).put(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
        }
    }

    private static void deleteOrder(Long id) throws IOException{
        String urlID = ORDERS_URL + "/" + id;
        Request request = new Request.Builder().url(urlID).delete().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка ответа сервера: " + response);
            }
        }
    }

    private static void createUI() {
        ImageIcon icon = new ImageIcon((PetstoreUI.class.getResource("/icons/pets.png")));
        Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        final ImageIcon iconPets = new ImageIcon(image);
        icon = new ImageIcon(PetstoreUI.class.getResource("/icons/orders.png"));
        image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        final ImageIcon iconOrders = new ImageIcon(image);

        JFrame frame = new JFrame("Petstore");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(colourBlue);

        final JLabel nameArea = new JLabel("Магазин животных");
        styleMenuText(nameArea);
        JButton petsButton = new JButton("Питомцы", iconPets);
        styleMenuButton(petsButton);
        JButton ordersButton = new JButton("Заказы", iconOrders);
        styleMenuButton(ordersButton);

        menuPanel.add(nameArea);
        menuPanel.add(petsButton);
        menuPanel.add(ordersButton);

        JPanel contentPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel placeholder = new JLabel("Выберите пункт меню");
        styleContentText(placeholder);
        contentPanel.add(placeholder);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, contentPanel);
        splitPane.setDividerLocation(200);

        frame.getContentPane().add(splitPane);
        frame.setVisible(true);

        JButton petsGetAllButton = new JButton("Посмотреть всех");
        styleBigContentButton(petsGetAllButton);
        JButton petsGetButton = new JButton("Найти");
        styleContentButton(petsGetButton);
        JButton petsCreateButton = new JButton("Создать");
        styleContentButton(petsCreateButton);
        JButton petsChangeButton = new JButton("Изменить");
        styleContentButton(petsChangeButton);
        JButton petsDeleteButton = new JButton("Удалить");
        styleContentButton(petsDeleteButton);

        JButton ordersGetAllButton = new JButton("Посмотреть все");
        styleBigContentButton(ordersGetAllButton);
        JButton ordersGetButton = new JButton("Найти");
        styleContentButton(ordersGetButton);
        JButton ordersCreateButton = new JButton("Создать");
        styleContentButton(ordersCreateButton);
        JButton ordersChangeButton = new JButton("Изменить");
        styleContentButton(ordersChangeButton);
        JButton ordersDeleteButton = new JButton("Удалить");
        styleContentButton(ordersDeleteButton);

        JButton backPetBtn = new JButton("Назад");
        styleBigContentButton(backPetBtn);
        backPetBtn.setPreferredSize(new Dimension(100, 30));
        JButton backOrderBtn = new JButton("Назад");
        styleBigContentButton(backOrderBtn);
        backOrderBtn.setPreferredSize(new Dimension(100, 30));

        JTable petTable = new JTable();
        JScrollPane scrollPanePet = new JScrollPane(petTable);

        JTable orderTable = new JTable();
        JScrollPane scrollPaneOrder = new JScrollPane(orderTable);

        petsButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);

            gbc.gridwidth = 2;
            contentPanel.add(petsGetAllButton, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            contentPanel.add(petsGetButton, gbc);

            gbc.gridx = 1;
            contentPanel.add(petsCreateButton, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            contentPanel.add(petsChangeButton, gbc);

            gbc.gridx = 1;
            contentPanel.add(petsDeleteButton, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        petsGetAllButton.addActionListener(e ->{
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);

            gbc.gridwidth = 2;
            contentPanel.add(backPetBtn, gbc);

            try {
                gbc.gridy = 1;
                gbc.gridx = 0;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                gbc.fill = GridBagConstraints.BOTH;

                List<PetResponse> pets = getAllPets();
                String[] columns = {"ID", "Имя", "Тип", "Дата рождения", "Владелец"};
                Object[][] data = new Object[pets.size()][5];
                for (int i = 0; i < pets.size(); i++) {
                    PetResponse p = pets.get(i);
                    data[i][0] = p.pet_id();
                    data[i][1] = p.pet_name();
                    data[i][2] = p.pet_type();
                    data[i][3] = p.birthdate().toString();
                    data[i][4] = p.pet_owner();
                }
                petTable.setModel(new DefaultTableModel(data, columns));
                styleTable(petTable);
                scrollPanePet.setBorder(BorderFactory.createLineBorder(colourGrey, 1));
                contentPanel.add(scrollPanePet, gbc);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Ошибка загрузки питомцев: " + ex.getMessage());
            }
        });

        petsGetButton.addActionListener(e ->{
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            contentPanel.add(backPetBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel label = new JLabel("Введите ID питомца:");
            styleContentText(label);
            contentPanel.add(label, gbc);

            gbc.gridy = 2;
            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(searchField, gbc);

            gbc.gridy = 2;
            gbc.gridx = 1;
            JButton searchBtn = new JButton("Найти");
            styleSearchBtn(searchBtn);
            contentPanel.add(searchBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            searchBtn.addActionListener(a ->{
                try{
                    gbc.gridwidth = 2;
                    gbc.gridy = 3;
                    gbc.gridx = 0;
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0;
                    gbc.fill = GridBagConstraints.BOTH;

                    String searchId = searchField.getText().trim();
                    if (searchId.isEmpty()){
                        JOptionPane.showMessageDialog(frame, "Введите ID");
                        return;
                    }
                    if (!searchId.matches("\\d+")){
                        JOptionPane.showMessageDialog(frame, "Введите число");
                        return;
                    }
                    PetResponse pet = getPet(searchId);
                    String[] columns = {"ID", "Имя", "Тип", "Дата рождения", "Владелец"};
                    Object[][] data = new Object[1][5];

                    data[0][0] = pet.pet_id();
                    data[0][1] = pet.pet_name();
                    data[0][2] = pet.pet_type();
                    data[0][3] = pet.birthdate().toString();
                    data[0][4] = pet.pet_owner();

                    petTable.setModel(new DefaultTableModel(data, columns));
                    styleTable(petTable);
                    scrollPanePet.setBorder(BorderFactory.createLineBorder(colourGrey, 1));
                    contentPanel.add(scrollPanePet, gbc);
                    contentPanel.revalidate();
                    contentPanel.repaint();

                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Ошибка загрузки питомца: " + ex.getMessage());
                }
            });
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        petsCreateButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            contentPanel.add(backPetBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel labelName = new JLabel("Имя питомца:");
            styleContentText(labelName);
            contentPanel.add(labelName, gbc);
            gbc.gridx = 1;
            JTextField fieldName = new JTextField();
            fieldName.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(fieldName, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            JLabel labelType = new JLabel("Тип питомца:");
            styleContentText(labelType);
            contentPanel.add(labelType, gbc);
            gbc.gridx = 1;
            JTextField fieldType = new JTextField();
            fieldType.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(fieldType, gbc);

            gbc.gridy = 3;
            gbc.gridx = 0;
            JLabel labelBirthdate = new JLabel("Дата рождения:");
            styleContentText(labelBirthdate);
            contentPanel.add(labelBirthdate, gbc);
            gbc.gridx = 1;
            JTextField fieldBirthdate = new JTextField();
            fieldBirthdate.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(fieldBirthdate, gbc);

            gbc.gridy = 4;
            gbc.gridx = 0;
            JLabel labelOwner = new JLabel("Хозяин питомца:");
            styleContentText(labelOwner);
            contentPanel.add(labelOwner, gbc);
            gbc.gridx = 1;
            JTextField fieldOwner = new JTextField();
            fieldOwner.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(fieldOwner, gbc);

            gbc.gridwidth = 2;
            gbc.gridy = 5;
            gbc.gridx = 0;
            JButton addBtn = new JButton("Добавить");
            styleBigContentButton(addBtn);
            addBtn.setPreferredSize(new Dimension(100, 30));
            contentPanel.add(addBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            addBtn.addActionListener(a -> {
                String name = fieldName.getText().trim();
                if (name.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите имя");
                    return;
                }
                if (!name.matches(NAME_REGEX)){
                    JOptionPane.showMessageDialog(frame, "Имя может состоять только из букв");
                    return;
                }

                String type = fieldType.getText().trim();
                if (type.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите тип питомца");
                    return;
                }
                if (!type.matches(NAME_REGEX)){
                    JOptionPane.showMessageDialog(frame, "Тип может состоять только из букв");
                    return;
                }

                String birthdateText = fieldBirthdate.getText().trim();
                if (birthdateText.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите дату рождения");
                    return;
                }
                if(!isValidTime(birthdateText)){
                    JOptionPane.showMessageDialog(frame, "Дата рождения должна быть в формате: 'ГГГГ-ММ-ДД'");
                    return;
                }
                LocalDate birthdate = LocalDate.parse(birthdateText);

                String owner = fieldOwner.getText().trim();
                if (!owner.isEmpty()){
                    if (!owner.matches(NAME_REGEX)){
                        JOptionPane.showMessageDialog(frame, "Имя может состоять только из букв");
                        return;
                    }
                }

                PetRequest request = new PetRequest(name, type, birthdate, owner);
                try{
                    createPet(request);
                    JOptionPane.showMessageDialog(frame, "Питомец успешно добавлен!");
                } catch(IOException ex){
                    JOptionPane.showMessageDialog(frame, "Ошибка загрузки питомцев: " + ex.getMessage());
                }

            });
        });

        petsChangeButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            contentPanel.add(backPetBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel label = new JLabel("Введите ID питомца:");
            styleContentText(label);
            contentPanel.add(label, gbc);

            gbc.gridy = 2;
            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(searchField, gbc);

            gbc.gridy = 2;
            gbc.gridx = 1;
            JButton searchBtn = new JButton("Найти");
            styleSearchBtn(searchBtn);
            contentPanel.add(searchBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            searchBtn.addActionListener(a -> {
                String searchId = searchField.getText().trim();
                if (searchId.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите ID");
                    return;
                }
                if (!searchId.matches("\\d+")){
                    JOptionPane.showMessageDialog(frame, "Введите число");
                    return;
                }
                try{
                    PetResponse pet = getPet(searchId);

                    gbc.gridx = 0;
                    gbc.gridy = 3;
                    JLabel labelName = new JLabel("Имя: " + pet.pet_name());
                    contentPanel.add(labelName, gbc);
                    gbc.gridx = 1;
                    JTextField fieldName = new JTextField();
                    fieldName.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldName, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 4;
                    JLabel labelType = new JLabel("Тип: " + pet.pet_type());
                    contentPanel.add(labelType, gbc);
                    gbc.gridx = 1;
                    JTextField fieldType = new JTextField();
                    fieldType.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldType, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 5;
                    JLabel labelBirthdate = new JLabel("Дата рождения: " + pet.birthdate().toString());
                    contentPanel.add(labelBirthdate, gbc);
                    gbc.gridx = 1;
                    JTextField fieldBirthdate = new JTextField();
                    fieldBirthdate.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldBirthdate, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 6;
                    JLabel labelOwner = new JLabel("Владелец: " + pet.pet_owner());
                    contentPanel.add(labelOwner, gbc);
                    gbc.gridx = 1;
                    JTextField fieldOwner = new JTextField();
                    fieldOwner.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldOwner, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 7;
                    gbc.gridwidth = 2;
                    JButton changeBtn = new JButton("Изменить");
                    styleBigContentButton(changeBtn);
                    contentPanel.add(changeBtn, gbc);

                    contentPanel.revalidate();
                    contentPanel.repaint();

                    changeBtn.addActionListener(b -> {
                        try{
                            String name = fieldName.getText().trim();
                            if (name.isEmpty()){
                                name = pet.pet_name();
                            }
                            if (!name.matches(NAME_REGEX)){
                                JOptionPane.showMessageDialog(frame, "Имя может состоять только из букв");
                                return;
                            }

                            String type = fieldType.getText().trim();
                            if (type.isEmpty()){
                                type = pet.pet_type();
                            }
                            if (!type.matches(NAME_REGEX)){
                                JOptionPane.showMessageDialog(frame, "Тип может состоять только из букв");
                                return;
                            }

                            String birthdateText = fieldBirthdate.getText().trim();
                            if (birthdateText.isEmpty()){
                                birthdateText = pet.birthdate().toString();
                            }
                            if(!isValidTime(birthdateText)){
                                JOptionPane.showMessageDialog(frame, "Дата рождения должна быть в формате: 'ГГГГ-ММ-ДД'");
                                return;
                            }
                            LocalDate birthdate = LocalDate.parse(birthdateText);

                            String owner = fieldOwner.getText().trim();
                            if (owner.isEmpty()){
                                owner = pet.pet_owner();
                            }
                            if (!owner.matches(NAME_REGEX)){
                                JOptionPane.showMessageDialog(frame, "Имя может состоять только из букв");
                                return;
                            }

                            PetRequest request = new PetRequest(name, type, birthdate, owner);
                            changePet(Long.parseLong(searchId), request);
                            JOptionPane.showMessageDialog(frame, "Питомец успешно изменён!");
                        } catch(IOException exception){
                            JOptionPane.showMessageDialog(frame, "Ошибка загрузки питомца: " + exception.getMessage());
                        }
                    });
                } catch(IOException ex){
                    JOptionPane.showMessageDialog(frame, "Ошибка загрузки питомца: " + ex.getMessage());
                }

            });
        });

        petsDeleteButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            contentPanel.add(backPetBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel label = new JLabel("Введите ID питомца:");
            styleContentText(label);
            contentPanel.add(label, gbc);

            gbc.gridy = 2;
            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(searchField, gbc);

            gbc.gridy = 2;
            gbc.gridx = 1;
            JButton deleteBtn = new JButton("Удалить");
            styleSearchBtn(deleteBtn);
            contentPanel.add(deleteBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            deleteBtn.addActionListener(a -> {
                String searchId = searchField.getText().trim();
                if (searchId.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите ID");
                    return;
                }
                if (!searchId.matches("\\d+")){
                    JOptionPane.showMessageDialog(frame, "Введите число");
                    return;
                }
                try {
                    deletePet(Long.parseLong(searchId));
                    JOptionPane.showMessageDialog(frame, "Питомец успешно удален!");
                } catch(IOException exception){
                    JOptionPane.showMessageDialog(frame, "Ошибка удаления питомца: " + exception.getMessage());
                }
            });
        });

        backPetBtn.addActionListener(e -> {
            petsButton.doClick();
        });

        ordersButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);

            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 0;
            contentPanel.add(ordersGetAllButton, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            contentPanel.add(ordersGetButton, gbc);

            gbc.gridx = 1;
            contentPanel.add(ordersCreateButton, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            contentPanel.add(ordersChangeButton, gbc);

            gbc.gridx = 1;
            contentPanel.add(ordersDeleteButton, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();
        });

        ordersGetAllButton.addActionListener(e ->{
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 0;
            contentPanel.add(backOrderBtn, gbc);

            try {
                gbc.gridy = 1;
                gbc.gridx = 0;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                gbc.fill = GridBagConstraints.BOTH;

                List<OrderResponse> orders = getAllOrders();
                String[] columns = {"ID", "ID Питомца", "Дата заказа", "Имя покупателя", "Статус"};
                Object[][] data = new Object[orders.size()][5];
                for (int i = 0; i < orders.size(); i++) {
                    OrderResponse p = orders.get(i);
                    data[i][0] = p.id();
                    data[i][1] = p.petId();
                    data[i][2] = p.orderDate().toString();
                    data[i][3] = p.customerName();
                    data[i][4] = p.orderStatus().toString();
                }
                orderTable.setModel(new DefaultTableModel(data, columns));
                styleTable(orderTable);
                scrollPaneOrder.setBorder(BorderFactory.createLineBorder(colourGrey, 1));
                contentPanel.add(scrollPaneOrder, gbc);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Ошибка загрузки заказов: " + ex.getMessage());
            }
        });

        ordersGetButton.addActionListener(e ->{
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);

            gbc.gridwidth = 2;
            contentPanel.add(backOrderBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel label = new JLabel("Введите ID заказа:");
            styleContentText(label);
            contentPanel.add(label, gbc);

            gbc.gridy = 2;
            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(searchField, gbc);

            gbc.gridy = 2;
            gbc.gridx = 1;
            JButton searchBtn = new JButton("Найти");
            styleSearchBtn(searchBtn);
            contentPanel.add(searchBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            searchBtn.addActionListener(a ->{
                try{
                    gbc.gridwidth = 2;
                    gbc.gridy = 3;
                    gbc.gridx = 0;
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0;
                    gbc.fill = GridBagConstraints.BOTH;

                    String searchId = searchField.getText().trim();
                    if (searchId.isEmpty()){
                        JOptionPane.showMessageDialog(frame, "Введите ID");
                        return;
                    }
                    OrderResponse order = getOrder(searchId);
                    String[] columns = {"ID", "ID Питомца", "Дата заказа", "Имя покупателя", "Статус"};
                    Object[][] data = new Object[1][5];

                    data[0][0] = order.id();
                    data[0][1] = order.petId();
                    data[0][2] = order.orderDate().toString();
                    data[0][3] = order.customerName();
                    data[0][4] = order.orderStatus().toString();

                    orderTable.setModel(new DefaultTableModel(data, columns));
                    styleTable(orderTable);
                    scrollPaneOrder.setBorder(BorderFactory.createLineBorder(colourGrey, 1));
                    contentPanel.add(scrollPaneOrder, gbc);
                    contentPanel.revalidate();
                    contentPanel.repaint();

                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Ошибка загрузки заказа: " + ex.getMessage());
                }
            });
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        ordersCreateButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            contentPanel.add(backOrderBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel labelPetId = new JLabel("ID питомца:");
            styleContentText(labelPetId);
            contentPanel.add(labelPetId, gbc);
            gbc.gridx = 1;
            JTextField fieldPetId = new JTextField();
            fieldPetId.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(fieldPetId, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            JLabel labelCustomerName = new JLabel("Имя покупателя:");
            styleContentText(labelCustomerName);
            contentPanel.add(labelCustomerName, gbc);
            gbc.gridx = 1;
            JTextField fieldCustomerName = new JTextField();
            fieldCustomerName.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(fieldCustomerName, gbc);

            gbc.gridy = 3;
            gbc.gridx = 0;
            JLabel labelOrderDate = new JLabel("Дата заказа:");
            styleContentText(labelOrderDate);
            contentPanel.add(labelOrderDate, gbc);
            gbc.gridx = 1;
            JTextField fieldOrderDate = new JTextField();
            fieldOrderDate.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(fieldOrderDate, gbc);

            gbc.gridwidth = 2;
            gbc.gridy = 4;
            gbc.gridx = 0;
            JButton addBtn = new JButton("Добавить");
            styleBigContentButton(addBtn);
            addBtn.setPreferredSize(new Dimension(100, 30));
            contentPanel.add(addBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            addBtn.addActionListener(a -> {
                String petIdText = fieldPetId.getText().trim();
                if (petIdText.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите ID питомца");
                    return;
                }
                if (!petIdText.matches("\\d+")){
                    JOptionPane.showMessageDialog(frame, "Введите число");
                    return;
                }
                Long petId = Long.parseLong(petIdText);

                String customerName = fieldCustomerName.getText().trim();
                if (customerName.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите имя покупателя");
                    return;
                }
                if (!customerName.matches(NAME_REGEX)){
                    JOptionPane.showMessageDialog(frame, "Имя покупателя может состоять только из букв");
                    return;
                }

                String orderDateText = fieldOrderDate.getText().trim();
                if (orderDateText.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите дату заказа");
                    return;
                }
                if (!isValidTime(orderDateText)){
                    JOptionPane.showMessageDialog(frame, "Дата заказа должна быть в формате: 'ГГГГ-ММ-ДД'");
                    return;
                }
                LocalDate orderDate = LocalDate.parse(orderDateText);

                OrderRequest request = new OrderRequest(petId, customerName, orderDate, Status.CREATED);
                try{
                    createOrder(request);
                    JOptionPane.showMessageDialog(frame, "Заказ успешно добавлен!");
                } catch(IOException ex){
                    JOptionPane.showMessageDialog(frame, "Ошибка добавления заказа: " + ex.getMessage());
                }

            });
        });

        ordersChangeButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            contentPanel.add(backOrderBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel label = new JLabel("Введите ID заказа:");
            styleContentText(label);
            contentPanel.add(label, gbc);

            gbc.gridy = 2;
            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(searchField, gbc);

            gbc.gridy = 2;
            gbc.gridx = 1;
            JButton searchBtn = new JButton("Найти");
            styleSearchBtn(searchBtn);
            contentPanel.add(searchBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            searchBtn.addActionListener(a -> {
                String searchId = searchField.getText().trim();
                if (searchId.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите ID");
                    return;
                }
                if (!searchId.matches("\\d+")){
                    JOptionPane.showMessageDialog(frame, "Введите число");
                    return;
                }
                try{
                    OrderResponse order = getOrder(searchId);

                    gbc.gridx = 0;
                    gbc.gridy = 3;
                    JLabel labelPetId = new JLabel("ID питомца: " + order.petId());
                    contentPanel.add(labelPetId, gbc);
                    gbc.gridx = 1;
                    JTextField fieldPetId = new JTextField();
                    fieldPetId.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldPetId, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 4;
                    JLabel labelCustomerName = new JLabel("Имя покупателя: " + order.customerName());
                    contentPanel.add(labelCustomerName, gbc);
                    gbc.gridx = 1;
                    JTextField fieldCustomerName = new JTextField();
                    fieldCustomerName.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldCustomerName, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 5;
                    JLabel labelOrderDate = new JLabel("Дата заказа: " + order.orderDate());
                    contentPanel.add(labelOrderDate, gbc);
                    gbc.gridx = 1;
                    JTextField fieldOrderDate = new JTextField();
                    fieldOrderDate.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldOrderDate, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 6;
                    JLabel labelStatus = new JLabel("Статус: " + order.orderStatus());
                    contentPanel.add(labelStatus, gbc);
                    gbc.gridx = 1;
                    JTextField fieldStatus = new JTextField();
                    fieldStatus.setPreferredSize(new Dimension(50, 30));
                    contentPanel.add(fieldStatus, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 7;
                    gbc.gridwidth = 2;
                    JButton changeBtn = new JButton("Изменить");
                    styleBigContentButton(changeBtn);
                    contentPanel.add(changeBtn, gbc);

                    contentPanel.revalidate();
                    contentPanel.repaint();

                    changeBtn.addActionListener(b -> {
                        try{
                            String petId = fieldPetId.getText().trim();
                            if (petId.isEmpty()){
                                petId = order.petId().toString();
                            }
                            if (!petId.matches("\\d+")){
                                JOptionPane.showMessageDialog(frame, "ID может состоять только из цифр");
                                return;
                            }

                            String customerName = fieldCustomerName.getText().trim();
                            if (customerName.isEmpty()){
                                customerName = order.customerName();
                            }
                            if (!customerName.matches(NAME_REGEX)){
                                JOptionPane.showMessageDialog(frame, "Тип может состоять только из букв");
                                return;
                            }

                            String orderDateText = fieldOrderDate.getText().trim();
                            if (orderDateText.isEmpty()){
                                orderDateText = order.orderDate().toString();
                            }
                            if(!isValidTime(orderDateText)){
                                JOptionPane.showMessageDialog(frame, "Дата заказа должна быть в формате: 'ГГГГ-ММ-ДД'");
                                return;
                            }
                            LocalDate orderDate = LocalDate.parse(orderDateText);

                            String status = fieldStatus.getText().trim();
                            Status enumStatus;
                            if (status.isEmpty()){
                                status = order.orderStatus().toString();
                            }
                            try {
                                enumStatus = Status.valueOf(status.toUpperCase());
                            } catch(IllegalArgumentException exception) {
                                JOptionPane.showMessageDialog(frame, "Статус может быть только 'CREATED', 'PROCESSING', 'COMPLETED'");
                                return;
                            }

                            OrderRequest request = new OrderRequest(Long.parseLong(petId), customerName, orderDate, enumStatus);
                            changeOrder(Long.parseLong(searchId), request);
                            JOptionPane.showMessageDialog(frame, "Заказ успешно изменён!");
                        } catch(IOException exception){
                            JOptionPane.showMessageDialog(frame, "Ошибка загрузки заказа: " + exception.getMessage());
                        }
                    });
                } catch(IOException ex){
                    JOptionPane.showMessageDialog(frame, "Ошибка загрузки заказа: " + ex.getMessage());
                }

            });
        });

        ordersDeleteButton.addActionListener(e -> {
            contentPanel.removeAll();
            setLayout(contentPanel, gbc);
            gbc.gridwidth = 2;
            contentPanel.add(backOrderBtn, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            JLabel label = new JLabel("Введите ID заказа:");
            styleContentText(label);
            contentPanel.add(label, gbc);

            gbc.gridy = 2;
            JTextField searchField = new JTextField();
            searchField.setPreferredSize(new Dimension(50, 30));
            contentPanel.add(searchField, gbc);

            gbc.gridy = 2;
            gbc.gridx = 1;
            JButton deleteBtn = new JButton("Удалить");
            styleSearchBtn(deleteBtn);
            contentPanel.add(deleteBtn, gbc);

            fillBtm(contentPanel);

            contentPanel.revalidate();
            contentPanel.repaint();

            deleteBtn.addActionListener(a -> {
                String searchId = searchField.getText().trim();
                if (searchId.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите ID");
                    return;
                }
                if (!searchId.matches("\\d+")){
                    JOptionPane.showMessageDialog(frame, "Введите число");
                    return;
                }
                try {
                    deleteOrder(Long.parseLong(searchId));
                    JOptionPane.showMessageDialog(frame, "Заказ успешно удален!");
                } catch(IOException exception){
                    JOptionPane.showMessageDialog(frame, "Ошибка удаления заказа: " + exception.getMessage());
                }
            });
        });

        backOrderBtn.addActionListener(e ->{
            ordersButton.doClick();
        });
    }
}
