//gui code
package edu.najah.software.gui;

import edu.najah.software.ai.AISummaryService;
import edu.najah.software.domain.Appointment;
import edu.najah.software.observer.EmailNotificationService;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.domain.appointmenttype.AppointmentType;
import edu.najah.software.observer.NotificationObserver;
import edu.najah.software.observer.NotificationService;
import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.repository.InMemoryAppointmentRepository;
import edu.najah.software.service.AppointmentService;
import edu.najah.software.service.AppointmentServiceImpl;
import edu.najah.software.service.AuthService;
import edu.najah.software.service.SimpleAuthService;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Supports two roles Admin (full access) and User (limited access)
 * After login automatically navigates to Available Slots
 *  Admin username: admin   password: admin123
 *  User username: user    password: user123
 */
public class AppointmentGUI extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color BG       = new Color(245, 246, 250);
    private static final Color SIDEBAR  = new Color(30, 40, 60);
    private static final Color ACCENT   = new Color(99, 102, 241);
    private static final Color ACCENT_H = new Color(79, 82, 221);
    private static final Color SUCCESS  = new Color(34, 197, 94);
    private static final Color DANGER   = new Color(239, 68, 68);
    private static final Color WARNING  = new Color(245, 158, 11);
    private static final Color CARD     = Color.WHITE;
    private static final Color TEXT_PRI = new Color(17, 24, 39);
    private static final Color TEXT_SEC = new Color(107, 114, 128);
    private static final Color BORDER_C = new Color(229, 231, 235);

    //services
    private final AuthService authService;
    private final AppointmentService appointmentService;
    private final AISummaryService aiSummaryService = new AISummaryService();

    //state
    private final List<String> notificationLog = new java.util.ArrayList<>();

    //main panels
    private JPanel cardContainer;
    private CardLayout cardLayout;

    // sidebar buttons shown/hidden by role
    private JButton slotsBtn;
    private JButton bookBtn;
    private JButton manageBtn;
    private JButton notifBtn;
    private JButton aiBtn;

    //login panel refs
    private JTextField loginUserField;
    private JPasswordField loginPassField;
    private JLabel loginStatusLabel;

    //email
    private JTextField recipientEmailField;

    //book panel refs
    private JTextField bookIdField;
    private JTextField bookDateField;
    private JTextField bookTimeField;
    private JSpinner bookDurationSpinner;
    private JSpinner bookParticipantsSpinner;
    private JComboBox<String> bookTypeCombo;
    private JLabel bookStatusLabel;

    //slots panel refs
    private JTextField slotsDateField;
    private DefaultTableModel slotsTableModel;

    //manage panel refs
    private DefaultTableModel apptTableModel;
    private JTextField modifyIdField;
    private JTextField modifyDateField;
    private JTextField modifyTimeField;
    private JTextField cancelIdField;
    private JLabel manageStatusLabel;
    private JButton modAdminBtn;
    private JButton cancelAdminBtn;

    //notifications panel
    private JTextArea notifArea;

    //status bar
    private JLabel statusBarLabel;
    private JLabel loginBadge;

     //Creates the gui
    public AppointmentGUI() {
        AppointmentRepository repository = new InMemoryAppointmentRepository();
        authService = new SimpleAuthService();
        appointmentService = new AppointmentServiceImpl(repository, authService);

        NotificationService guiNotifService = (appointment, message) -> {
            String entry = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + message;
            notificationLog.add(entry);
            if (notifArea != null) notifArea.append(entry + "\n");
        };
        appointmentService.addObserver(new NotificationObserver(guiNotifService));

        buildFrame(); }

    //FRAME
    private void buildFrame() {
        setTitle("Appointment Scheduling System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        updateUIForRole();
    }

    //SIDEBAR
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(210, 0));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SIDEBAR);
        header.setBorder(new EmptyBorder(24, 20, 20, 20));
        JLabel title = new JLabel("AppScheduler");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel sub = new JLabel("v1.0");
        sub.setForeground(new Color(148, 163, 184));
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        header.add(title, BorderLayout.NORTH);
        header.add(sub, BorderLayout.SOUTH);
        sidebar.add(header);
        sidebar.add(makeSidebarDivider());
        sidebar.add(makeSidebarBtn("🔐  Login / Logout", "login"));

        slotsBtn  = makeSidebarBtn("📅  Available Slots",    "slots");
        bookBtn   = makeSidebarBtn("➕  Book Appointment",   "book");
        manageBtn = makeSidebarBtn("📋  Manage Appointments","manage");
        notifBtn  = makeSidebarBtn("🔔  Notifications",      "notif");
        aiBtn     = makeSidebarBtn("🤖  AI Summary",         "ai");

        sidebar.add(slotsBtn);
        sidebar.add(bookBtn);
        sidebar.add(manageBtn);
        sidebar.add(notifBtn);
        sidebar.add(aiBtn);

        sidebar.add(Box.createVerticalGlue());
        loginBadge = new JLabel("● Not logged in");
        loginBadge.setForeground(DANGER);
        loginBadge.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginBadge.setBorder(new EmptyBorder(12, 20, 20, 20));
        loginBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(loginBadge);
        return sidebar;
    }

    private Component makeSidebarDivider() {
        JPanel d = new JPanel();
        d.setBackground(new Color(50, 65, 90));
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        d.setPreferredSize(new Dimension(0, 1));
        return d;
    }
    private JButton makeSidebarBtn(String text, String card) {
        JButton btn = new JButton(text);
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(SIDEBAR);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(45, 60, 85));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(SIDEBAR);
                btn.setForeground(new Color(203, 213, 225));
            }
        });
        btn.addActionListener(e -> {
            cardLayout.show(cardContainer, card);
            if (card.equals("manage")) refreshAppointmentsTable();
            if (card.equals("notif"))  refreshNotifPanel();
        });
        return btn; }

    //MAIN CARD AREA
    private JPanel buildMainArea() {
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(BG);
        cardContainer.add(buildLoginPanel(),  "login");
        cardContainer.add(buildSlotsPanel(),  "slots");
        cardContainer.add(buildBookPanel(),   "book");
        cardContainer.add(buildManagePanel(), "manage");
        cardContainer.add(buildNotifPanel(),  "notif");
        cardContainer.add(buildAIPanel(),     "ai");
        cardLayout.show(cardContainer, "login");
        return cardContainer;
    }

    //STATUS BAR
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        bar.setBackground(new Color(241, 245, 249));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_C));
        statusBarLabel = new JLabel("Welcome!  Admin: admin / admin123     User: user / user123");
        statusBarLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusBarLabel.setForeground(TEXT_SEC);
        bar.add(statusBarLabel);
        return bar;
    }

    private void setStatus(String msg, Color color) {
        statusBarLabel.setText(msg);
        statusBarLabel.setForeground(color); }

    //LOGIN PANEL
    private JPanel buildLoginPanel() {
        JPanel outer = centeredCard();

        JPanel card = makeCard(440, -1);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 32, 28, 32));

        card.add(makeCardTitle("Login"));
        card.add(Box.createVerticalStrut(8));

        // credentials hint box
        JPanel hintBox = new JPanel();
        hintBox.setLayout(new BoxLayout(hintBox, BoxLayout.Y_AXIS));
        hintBox.setBackground(new Color(238, 242, 255));
        hintBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(199, 210, 254)),
            new EmptyBorder(10, 14, 10, 14)
        ));
        hintBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel hint1 = new JLabel("Admin — username: admin   password: admin123");
        JLabel hint2 = new JLabel("User  — username: user    password: user123");
        hint1.setFont(new Font("Monospaced", Font.PLAIN, 12));
        hint2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        hint1.setForeground(new Color(67, 56, 202));
        hint2.setForeground(new Color(67, 56, 202));
        hintBox.add(hint1);
        hintBox.add(Box.createVerticalStrut(4));
        hintBox.add(hint2);
        card.add(hintBox);
        card.add(Box.createVerticalStrut(18));

        card.add(makeLabel("Username", TEXT_PRI));
        card.add(Box.createVerticalStrut(4));
        loginUserField = makeTextField("admin");
        card.add(loginUserField);
        card.add(Box.createVerticalStrut(10));

        card.add(makeLabel("Password", TEXT_PRI));
        card.add(Box.createVerticalStrut(4));
        loginPassField = new JPasswordField("admin123");
        styleTextField(loginPassField);
        card.add(loginPassField);
        card.add(Box.createVerticalStrut(16));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(CARD);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        JButton loginBtn = makeAccentBtn("Login");
        loginBtn.addActionListener(e -> doLogin());
        JButton logoutBtn = makeDangerBtn("Logout");
        logoutBtn.addActionListener(e -> doLogout());
        btnRow.add(loginBtn);
        btnRow.add(logoutBtn);
        card.add(btnRow);
        card.add(Box.createVerticalStrut(12));

        loginStatusLabel = new JLabel(" ");
        loginStatusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        loginStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(loginStatusLabel);
        outer.add(card);
        return outer; }

    private void doLogin() {
        String user = loginUserField.getText().trim();
        String pass = new String(loginPassField.getPassword());
        if (authService.login(user, pass)) {
            updateUIForRole();
            String role = authService.isAdmin() ? "Admin" : "User";
            loginStatusLabel.setForeground(SUCCESS);
            loginStatusLabel.setText("✓ Logged in as " + role);
            setStatus("Logged in as " + role + " (" + user + ")", SUCCESS);
            // navigate based on role
            if (authService.isAdmin()) {
                cardLayout.show(cardContainer, "slots");
                doViewSlots();
            } else {
                cardLayout.show(cardContainer, "book");
            }
        } else {
            loginStatusLabel.setForeground(DANGER);
            loginStatusLabel.setText("✗ Invalid credentials");
            setStatus("Login failed — check your credentials", DANGER);
        }
    }

    private void doLogout() {
        authService.logout();
        updateUIForRole();
        loginStatusLabel.setForeground(WARNING);
        loginStatusLabel.setText("Logged out successfully");
        setStatus("Logged out", WARNING);
        cardLayout.show(cardContainer, "login");
    }

     //Shows/hides sidebar buttons and admin-only controls based on current role.
    
    private void updateUIForRole() {
        boolean loggedIn = authService.isLoggedIn();
        boolean isAdmin  = authService.isAdmin();
        slotsBtn.setVisible(loggedIn);
        bookBtn.setVisible(loggedIn);
        manageBtn.setVisible(loggedIn);
        notifBtn.setVisible(loggedIn);
        aiBtn.setVisible(loggedIn);

        if (modAdminBtn    != null) modAdminBtn.setVisible(isAdmin);
        if (cancelAdminBtn != null) cancelAdminBtn.setVisible(isAdmin);

        if (!loggedIn) {
            loginBadge.setText("● Not logged in");
            loginBadge.setForeground(DANGER);
        } else if (isAdmin)
        {
            loginBadge.setText("● Admin");
            loginBadge.setForeground(SUCCESS);
        } 
        else
        {
            loginBadge.setText("● User");
            loginBadge.setForeground(new Color(99, 102, 241)); } }

    // SLOTS PANEL
    private JPanel buildSlotsPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(32, 32, 32, 32));
        outer.add(makePanelHeader("Available Slots", "View free time slots for any date"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BG);
        JPanel controls = makeCard(-1, -1);
        controls.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        controls.add(makeLabel("Date (yyyy-MM-dd):", TEXT_PRI));
        slotsDateField = makeTextField(LocalDate.now().toString());
        slotsDateField.setPreferredSize(new Dimension(140, 34));
        controls.add(slotsDateField);
        JButton viewBtn = makeAccentBtn("View Slots");
        viewBtn.addActionListener(e -> doViewSlots());
        controls.add(viewBtn);
        content.add(controls, BorderLayout.NORTH);

        String[] cols = {"#", "Date", "Start", "End", "Status"};
        slotsTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = makeStyledTable(slotsTableModel);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        outer.add(content, BorderLayout.CENTER);
        return outer; }

    private void doViewSlots() {
        try {
            LocalDate date = LocalDate.parse(slotsDateField.getText().trim());
            List<TimeSlot> slots = appointmentService.getAvailableSlots(date);
            slotsTableModel.setRowCount(0);
            int i = 1;
            for (TimeSlot s : slots) {
                slotsTableModel.addRow(new Object[]{i++, s.getDate(), s.getStart(), s.getEnd(), "Available"});
            }
            setStatus("Showing " + slots.size() + " available slots for " + date, SUCCESS);
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    //BOOK PANEL
    private JPanel buildBookPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(32, 32, 32, 32));

        outer.add(makePanelHeader("Book Appointment", "Fill in the details below to create a new appointment"), BorderLayout.NORTH);

        JPanel card = makeCard(-1, -1);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 8, 6, 8);

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0;
        card.add(makeLabel("Appointment ID", TEXT_PRI), gc);
        gc.gridx = 1; gc.weightx = 1;
        bookIdField = makeTextField("APT-" + (int)(Math.random() * 9000 + 1000));
        card.add(bookIdField, gc);

        gc.gridx = 0; gc.gridy = 1; gc.weightx = 0;
        card.add(makeLabel("Date (yyyy-MM-dd)", TEXT_PRI), gc);
        gc.gridx = 1; gc.weightx = 1;
        bookDateField = makeTextField(LocalDate.now().plusDays(1).toString());
        card.add(bookDateField, gc);

        gc.gridx = 0; gc.gridy = 2; gc.weightx = 0;
        card.add(makeLabel("Time (HH:mm)", TEXT_PRI), gc);
        gc.gridx = 1; gc.weightx = 1;
        bookTimeField = makeTextField("09:00");
        card.add(bookTimeField, gc);

        gc.gridx = 0; gc.gridy = 3; gc.weightx = 0;
        card.add(makeLabel("Duration (minutes, max 120)", TEXT_PRI), gc);
        gc.gridx = 1; gc.weightx = 1;
        bookDurationSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 120, 15));
        styleSpinner(bookDurationSpinner);
        card.add(bookDurationSpinner, gc);

        gc.gridx = 0; gc.gridy = 4; gc.weightx = 0;
        card.add(makeLabel("Participants (max 5)", TEXT_PRI), gc);
        gc.gridx = 1; gc.weightx = 1;
        bookParticipantsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        styleSpinner(bookParticipantsSpinner);
        card.add(bookParticipantsSpinner, gc);

        gc.gridx = 0; gc.gridy = 5; gc.weightx = 0;
        card.add(makeLabel("Appointment Type", TEXT_PRI), gc);
        gc.gridx = 1; gc.weightx = 1;
        String[] types = {"None (general)", "URGENT", "FOLLOW_UP", "ASSESSMENT", "VIRTUAL", "IN_PERSON", "INDIVIDUAL", "GROUP"};
        bookTypeCombo = new JComboBox<>(types);
        bookTypeCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        card.add(bookTypeCombo, gc);

        gc.gridx = 0; gc.gridy = 6; gc.gridwidth = 1; gc.weightx = 0;
        card.add(makeLabel("Recipient Email (for notification)", TEXT_PRI), gc);
        gc.gridx = 1; gc.weightx = 1;
        recipientEmailField = makeTextField("recipient@example.com");
        card.add(recipientEmailField, gc);

        gc.gridx = 0; gc.gridy = 7; gc.gridwidth = 2;
        JLabel hints = makeLabel("Rules — URGENT: ≤30min, ≤2 people | GROUP: ≤120min, 3-5 people | INDIVIDUAL: ≤60min, 1 person", TEXT_SEC);
        hints.setFont(new Font("SansSerif", Font.ITALIC, 11));
        card.add(hints, gc);

        gc.gridx = 0; gc.gridy = 8; gc.gridwidth = 2;
        gc.insets = new Insets(16, 8, 6, 8);
        JButton bookBtnAction = makeAccentBtn("Book Appointment");
        bookBtnAction.addActionListener(e -> doBook());
        card.add(bookBtnAction, gc);

        gc.gridy = 9; gc.insets = new Insets(6, 8, 6, 8);
        bookStatusLabel = new JLabel(" ");
        bookStatusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        card.add(bookStatusLabel, gc);

        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    private void doBook() {
        try {
            String id = bookIdField.getText().trim();
            if (id.isEmpty()) id = "APT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            LocalDate date = LocalDate.parse(bookDateField.getText().trim());
            String[] timeParts = bookTimeField.getText().trim().split(":");
            LocalDateTime dateTime = date.atTime(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
            int duration     = (int) bookDurationSpinner.getValue();
            int participants = (int) bookParticipantsSpinner.getValue();
            String typeStr   = (String) bookTypeCombo.getSelectedItem();

            Appointment appt;
            if (typeStr == null || typeStr.startsWith("None")) {
                appt = appointmentService.bookAppointment(id, dateTime, duration, participants);
            } else {
                AppointmentType type = AppointmentType.valueOf(typeStr);
                appt = appointmentService.bookAppointment(id, dateTime, duration, participants, type);
            }

            bookStatusLabel.setForeground(SUCCESS);
            bookStatusLabel.setText("✓ Booked! ID: " + appt.getId() + " | Status: " + appt.getStatus());
            setStatus("Appointment booked successfully: " + appt.getId(), SUCCESS);

            String recipient = recipientEmailField.getText().trim();
            if (!recipient.isEmpty() && recipient.contains("@") && !recipient.equals("recipient@example.com")) {
                new Thread(() -> {
                    try {
                        new EmailNotificationService(recipient)
                            .sendNotification(appt, "Your appointment has been booked successfully.");
                    } catch (Exception ex) {
                        System.err.println("Email error: " + ex.getMessage());
                    }
                }).start();
            }

            bookIdField.setText("APT-" + (int)(Math.random() * 9000 + 1000));

//refresh slots table so it reflects the newly booked slot
            doViewSlots();

        } catch (Exception ex) {
            bookStatusLabel.setForeground(DANGER);
            bookStatusLabel.setText("✗ " + ex.getMessage());
            setStatus("Booking failed: " + ex.getMessage(), DANGER);
        }
    }

    //MANAGE PANEL

    private JPanel buildManagePanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(BG);
        headerRow.add(makePanelHeader("Manage Appointments", "View, modify, or cancel existing appointments"), BorderLayout.CENTER);
        JButton refreshBtn = makeSecondaryBtn("↻  Refresh Table");
        refreshBtn.addActionListener(e -> refreshAppointmentsTable());
        JPanel refreshWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        refreshWrap.setBackground(BG);
        refreshWrap.add(refreshBtn);
        headerRow.add(refreshWrap, BorderLayout.EAST);
        outer.add(headerRow, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBackground(BG);

        String[] cols = {"ID", "Type", "Date & Time", "Duration (min)", "Participants", "Status"};
        apptTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = makeStyledTable(apptTableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(0, 150));
        content.add(scroll, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(1, 2, 12, 0));
        actions.setBackground(BG);

        //modify card
        JPanel modCard = makeCard(-1, -1);
        modCard.setLayout(new BoxLayout(modCard, BoxLayout.Y_AXIS));
        modCard.setBorder(new EmptyBorder(12, 14, 12, 14));
        modCard.add(makeCardTitle("Modify Appointment"));
        modCard.add(Box.createVerticalStrut(8));
        modCard.add(makeLabel("Appointment ID", TEXT_PRI));
        modCard.add(Box.createVerticalStrut(3));
        modifyIdField = makeTextField("");
        modCard.add(modifyIdField);
        modCard.add(Box.createVerticalStrut(6));
        modCard.add(makeLabel("New Date (yyyy-MM-dd)", TEXT_PRI));
        modCard.add(Box.createVerticalStrut(3));
        modifyDateField = makeTextField(LocalDate.now().plusDays(2).toString());
        modCard.add(modifyDateField);
        modCard.add(Box.createVerticalStrut(6));
        modCard.add(makeLabel("New Time (HH:mm)", TEXT_PRI));
        modCard.add(Box.createVerticalStrut(3));
        modifyTimeField = makeTextField("10:00");
        modCard.add(modifyTimeField);
        modCard.add(Box.createVerticalStrut(10));

        JButton modUserBtn = makeAccentBtn("Modify (User)");
        modUserBtn.addActionListener(e -> doModify(false));
        modUserBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        modCard.add(modUserBtn);
        modCard.add(Box.createVerticalStrut(6));

        modAdminBtn = makeSecondaryBtn("Modify (Admin)");
        modAdminBtn.addActionListener(e -> doModify(true));
        modAdminBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        modCard.add(modAdminBtn);
        actions.add(modCard);

      //cancel card
        JPanel cancelCard = makeCard(-1, -1);
        cancelCard.setLayout(new BoxLayout(cancelCard, BoxLayout.Y_AXIS));
        cancelCard.setBorder(new EmptyBorder(12, 14, 12, 14));
        cancelCard.add(makeCardTitle("Cancel Appointment"));
        cancelCard.add(Box.createVerticalStrut(8));
        cancelCard.add(makeLabel("Appointment ID", TEXT_PRI));
        cancelCard.add(Box.createVerticalStrut(3));
        cancelIdField = makeTextField("");
        cancelCard.add(cancelIdField);
        cancelCard.add(Box.createVerticalStrut(10));

        JButton cancelUserBtn = makeDangerBtn("Cancel (User)");
        cancelUserBtn.addActionListener(e -> doCancel(false));
        cancelUserBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cancelCard.add(cancelUserBtn);
        cancelCard.add(Box.createVerticalStrut(6));

        cancelAdminBtn = makeSecondaryBtn("Cancel (Admin)");
        cancelAdminBtn.addActionListener(e -> doCancel(true));
        cancelAdminBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cancelCard.add(cancelAdminBtn);
        cancelCard.add(Box.createVerticalGlue());
        actions.add(cancelCard);

        content.add(actions, BorderLayout.CENTER);

        manageStatusLabel = new JLabel(" ");
        manageStatusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        manageStatusLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
        content.add(manageStatusLabel, BorderLayout.SOUTH);

        outer.add(content, BorderLayout.CENTER);
        return outer;
    }

    private void refreshAppointmentsTable() {
        apptTableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Appointment a : appointmentService.getAllAppointments()) {
            apptTableModel.addRow(new Object[]{
                a.getId(),
                a.getType() != null ? a.getType() : "General",
                a.getDateTime().format(fmt),
                a.getDuration(),
                a.getParticipants(),
                a.getStatus()
            });
        } }

    private void doModify(boolean asAdmin) {
        try {
            String id = modifyIdField.getText().trim();
            LocalDate date = LocalDate.parse(modifyDateField.getText().trim());
            String[] t = modifyTimeField.getText().trim().split(":");
            LocalDateTime newDT = date.atTime(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
            if (asAdmin) {
                appointmentService.adminModifyAppointment(id, newDT);
                manageStatusLabel.setForeground(SUCCESS);
                manageStatusLabel.setText("✓ Admin modified appointment " + id);
            } else {
                appointmentService.modifyAppointment(id, newDT);
                manageStatusLabel.setForeground(SUCCESS);
                manageStatusLabel.setText("✓ Modified appointment " + id);
            }
            refreshAppointmentsTable();
            setStatus("Appointment " + id + " rescheduled", SUCCESS);
        } catch (Exception ex) {
            manageStatusLabel.setForeground(DANGER);
            manageStatusLabel.setText("✗ " + ex.getMessage());
            setStatus(ex.getMessage(), DANGER);
        }
        }

    private void doCancel(boolean asAdmin) {
        try {
            String id = cancelIdField.getText().trim();
            if (asAdmin) {
                appointmentService.adminCancelAppointment(id);
                manageStatusLabel.setForeground(SUCCESS);
                manageStatusLabel.setText("✓ Admin cancelled appointment " + id);
            } else {
                appointmentService.cancelAppointment(id);
                manageStatusLabel.setForeground(SUCCESS);
                manageStatusLabel.setText("✓ Cancelled appointment " + id);
            }
            refreshAppointmentsTable();
            setStatus("Appointment " + id + " cancelled", WARNING);
        } catch (Exception ex) {
            manageStatusLabel.setForeground(DANGER);
            manageStatusLabel.setText("✗ " + ex.getMessage());
            setStatus(ex.getMessage(), DANGER);
        }   }

  //NOTIFICATIONS PANEL

    private JPanel buildNotifPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(32, 32, 32, 32));

        outer.add(makePanelHeader("Notifications", "Live log of all system notifications"), BorderLayout.NORTH);

        JPanel card = makeCard(-1, -1);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        notifArea = new JTextArea();
        notifArea.setEditable(false);
        notifArea.setLineWrap(true);
        notifArea.setWrapStyleWord(true);
        notifArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        notifArea.setBackground(new Color(248, 250, 252));
        notifArea.setForeground(TEXT_PRI);
        notifArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        notifArea.setText("No notifications yet.\n");

        JScrollPane scroll = new JScrollPane(notifArea);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_C));
        card.add(scroll, BorderLayout.CENTER);

        JButton clearBtn = makeSecondaryBtn("Clear");
        clearBtn.addActionListener(e -> { notifArea.setText(""); notificationLog.clear(); });
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        btnRow.setBackground(CARD);
        btnRow.add(clearBtn);
        card.add(btnRow, BorderLayout.SOUTH);

        outer.add(card, BorderLayout.CENTER);
        return outer; }

    private void refreshNotifPanel() {
        if (notifArea != null && notificationLog.isEmpty())
            notifArea.setText("No notifications yet.\n"); }

  //AI SUMMARY PANEL
    private JPanel buildAIPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(32, 32, 32, 32));

        outer.add(makePanelHeader("AI Summary", "AI-powered analysis of your appointments using Groq / Llama 3"), BorderLayout.NORTH);

        JPanel card = makeCard(-1, -1);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JTextArea aiArea = new JTextArea();
        aiArea.setEditable(false);
        aiArea.setLineWrap(true);
        aiArea.setWrapStyleWord(true);
        aiArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        aiArea.setBackground(new Color(248, 250, 252));
        aiArea.setForeground(TEXT_PRI);
        aiArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        aiArea.setText("Click 'Generate AI Summary' to analyze your appointments.");

        JScrollPane scroll = new JScrollPane(aiArea);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_C));
        card.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        bottom.setBackground(CARD);

        JButton generateBtn = makeAccentBtn("🤖  Generate AI Summary");
        JLabel aiStatusLabel = new JLabel(" ");
        aiStatusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        aiStatusLabel.setForeground(TEXT_SEC);
        aiStatusLabel.setBorder(new EmptyBorder(0, 12, 0, 0));

        generateBtn.addActionListener(e -> {
            List<Appointment> all = appointmentService.getAllAppointments();
            if (all.isEmpty()) {
                aiArea.setText("No appointments found. Book some first.");
                return;
            }
            generateBtn.setEnabled(false);
            aiArea.setText("Generating AI summary, please wait...");
            aiStatusLabel.setText("Connecting to AI...");
            new Thread(() -> {
                String result = aiSummaryService.generateSummary(all);
                SwingUtilities.invokeLater(() -> {
                    aiArea.setText(result);
                    aiArea.setCaretPosition(0);
                    generateBtn.setEnabled(true);
                    aiStatusLabel.setText("Done! Generated for " + all.size() + " appointment(s).");
                    aiStatusLabel.setForeground(SUCCESS);
                });
            }).start();
        });

        bottom.add(generateBtn);
        bottom.add(aiStatusLabel);
        card.add(bottom, BorderLayout.SOUTH);
        outer.add(card, BorderLayout.CENTER);
        return outer; }

    //  HELPERS
    private JPanel centeredCard() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        return p;
    }

    private JPanel makeCard(int w, int h) {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C),
            new EmptyBorder(0, 0, 0, 0)
        ));
        if (w > 0 || h > 0)
            card.setPreferredSize(new Dimension(w > 0 ? w : 400, h > 0 ? h : 300));
        return card;
    }

    private JPanel makePanelHeader(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(0, 0, 20, 0));
        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 22));
        t.setForeground(TEXT_PRI);
        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("SansSerif", Font.PLAIN, 13));
        s.setForeground(TEXT_SEC);
        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(s);
        return p;
    }

    private JLabel makeCardTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 15));
        l.setForeground(TEXT_PRI);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel makeLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField makeTextField(String value) {
        JTextField f = new JTextField(value);
        styleTextField(f);
        return f;
    }

    private void styleTextField(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C),
            new EmptyBorder(6, 10, 6, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleSpinner(JSpinner s) {
        s.setFont(new Font("SansSerif", Font.PLAIN, 13));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        ((JSpinner.DefaultEditor) s.getEditor()).getTextField().setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private JButton makeAccentBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(ACCENT_H); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(ACCENT); }
        });
        return b;
    }

    private JButton makeDangerBtn(String text) {
        JButton b = makeAccentBtn(text);
        b.setBackground(DANGER);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(220, 50, 50)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(DANGER); }
        });
        return b;
    }

    private JButton makeSecondaryBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(CARD);
        b.setForeground(TEXT_PRI);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C),
            new EmptyBorder(8, 16, 8, 16)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    private JTable makeStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(249, 250, 251));
        table.getTableHeader().setForeground(TEXT_SEC);
        table.setGridColor(BORDER_C);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(238, 242, 255));
        table.setSelectionForeground(TEXT_PRI);
        table.setFillsViewportHeight(true);
        return table;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * @param args command line arguments 
     */
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new AppointmentGUI().setVisible(true));
    }
}