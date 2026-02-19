package com.opurex.ortus.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.mpowa.android.sdk.powapos.core.PowaPOSEnums;
import com.aclas.ortus.BluetoothScaleSelectionActivity;
import com.opurex.ortus.client.activities.POSConnectedTrackedActivity;
import com.opurex.ortus.client.data.Data;
import com.opurex.ortus.client.drivers.POSDeviceManager;
import com.opurex.ortus.client.drivers.printer.documents.OrderDocument;
import com.opurex.ortus.client.drivers.printer.documents.ReceiptDocument;
import com.opurex.ortus.client.drivers.utils.DeviceManagerEvent;
import com.opurex.ortus.client.fragments.CatalogFragment;
import com.opurex.ortus.client.fragments.CustomerInfoDialog;
import com.opurex.ortus.client.fragments.CustomerSelectDialog;
import com.opurex.ortus.client.fragments.DividerDialog;
import com.opurex.ortus.client.fragments.ManualInputDialog;
import com.opurex.ortus.client.fragments.PaymentFragment;
import com.opurex.ortus.client.fragments.PaymentMainFragment;
import com.opurex.ortus.client.fragments.ProductScaleDialog;
import com.opurex.ortus.client.fragments.TicketFragment;
import com.opurex.ortus.client.fragments.TicketLineEditDialog;
import com.opurex.ortus.client.fragments.TransactionMainFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.opurex.ortus.client.services.P2PPaymentService;
import com.opurex.ortus.client.models.Barcode;
import com.opurex.ortus.client.models.Catalog;
import com.opurex.ortus.client.models.Category;
import com.opurex.ortus.client.models.CompositionInstance;
import com.opurex.ortus.client.models.Customer;
import com.opurex.ortus.client.models.Discount;
import com.opurex.ortus.client.models.Payment;
import com.opurex.ortus.client.models.Product;
import com.opurex.ortus.client.models.Receipt;
import com.opurex.ortus.client.models.Session;
import com.opurex.ortus.client.models.TariffArea;
import com.opurex.ortus.client.models.Ticket;
import com.opurex.ortus.client.models.TicketLine;
import com.opurex.ortus.client.models.User;
import com.opurex.ortus.client.utils.BarcodeCheck;
import com.opurex.ortus.client.utils.Error;
import com.opurex.ortus.client.utils.OpurexConfiguration;
import com.opurex.ortus.client.utils.ScaleManager;
import com.opurex.ortus.client.utils.exception.NotFoundException;

import java.io.IOError;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;


public class Transaction extends POSConnectedTrackedActivity
        implements CatalogFragment.Listener,
        ProductScaleDialog.Listener,
        ManualInputDialog.Listener,
        TicketLineEditDialog.Listener,
        TicketFragment.Listener,
        PaymentFragment.Listener,
        CustomerSelectDialog.Listener,
        CustomerInfoDialog.CustomerListener,
        DividerDialog.RequestResultListener, DividerDialog.ResultListener {


    // Activity Result code
    private static final int COMPOSITION = 1;
    private static final int CUSTOMER_SELECT = 2;
    private static final int CUSTOMER_CREATE = 3;
    private static final int RESTAURANT_TICKET_FINISH = 4;

    private static final String LOG_TAG = "Opurex/Transaction";
    private static final int CATALOG_FRAG = 0;
    private static final int TICKET_FRAG = 1;
    private static final int PAYMENT_FRAG = 2;
    private static final long SCANNERTIMER = 500;
    public static final int PAST_TICKET_FOR_RESULT = 0;


    // Data
    private Context mContext;
    private MaterialToolbar toolbar;
    private Ticket mPendingTicket;
    private TransactionPagerAdapter mPagerAdapter;

    // Views
    private ViewPager2 mPager;
    private String barcode = "";
    private long lastBarCodeTime;
    private CustomerInfoDialog customerInfoDialog;
    private ScaleManager scaleManager;


    private static final TransPage[] PAGES = new TransPage[]{
            new TransPage(TransactionMainFragment.class), // Page 0
            new TransPage(PaymentMainFragment.class)          // Page 1
    };


    // Adapter
    public static class TransactionPagerAdapter extends FragmentStateAdapter {
        private final TransPage[] pages;
        private final Fragment[] fragments;

        public TransactionPagerAdapter(@NonNull FragmentActivity activity, TransPage[] pages) {
            super(activity);
            this.pages = pages;
            this.fragments = new Fragment[pages.length]; // allocate array to hold fragments
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (fragments[position] == null) {
                try {
                    Class<? extends Fragment> clazz = pages[position].getFragment();
                    Method newInstance = clazz.getMethod("newInstance", int.class);
                    fragments[position] = (Fragment) newInstance.invoke(null, position);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot create fragment for position: " + position, e);
                }
            }
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return pages.length;
        }

        // ✅ Add this method so you can access fragments from outside
        public <T extends Fragment> T getFragment(int position, Class<T> clazz) {
            Fragment fragment = fragments[position];
            if (clazz.isInstance(fragment)) {
                return clazz.cast(fragment);
            }
            return null;
        }
    }

    // Simple fragment definition
    public static class TransPage {
        private final Class<? extends Fragment> mFragment;

        public TransPage(@NonNull Class<? extends Fragment> fragment) {
            this.mFragment = fragment;
        }

        public Class<? extends Fragment> getFragment() {
            return mFragment;
        }
    }


    //  FUNCTIONS
    private final BroadcastReceiver p2pReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (P2PPaymentService.ACTION_TICKET_RECEIVED.equals(intent.getAction())) {
                runOnUiThread(() -> {
                    Toast.makeText(Transaction.this, "New ticket received via P2P", Toast.LENGTH_SHORT).show();
                    TicketFragment ticketFragment = getTicketFragment();
                    if (ticketFragment != null) {
                        ticketFragment.updateView();
                    }
                });
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mContext = this;
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        enableActionBarTitle();

        mPager = findViewById(R.id.transaction_view_pager);
        mPagerAdapter = new TransactionPagerAdapter(this, PAGES);
        mPager.setAdapter(mPagerAdapter);
        mPager.setBackgroundResource(R.color.main_bg);

        mPager.setClipToPadding(false);
        mPager.setClipChildren(false);
        mPager.setOffscreenPageLimit(PAGES.length);
        mPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        mPager.registerOnPageChangeCallback(pageChangeCallback);
        
        // Initialize the scale manager
        initializeScaleManager();

        LocalBroadcastManager.getInstance(this).registerReceiver(p2pReceiver,
                new IntentFilter(P2PPaymentService.ACTION_TICKET_RECEIVED));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (customerInfoDialog != null && customerInfoDialog.isVisible()) {
            customerInfoDialog.looseKeyboardFocus();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!returnToCatalogueView()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PAST_TICKET_FOR_RESULT:
                if (data != null) {
                    String ticketId = data.getStringExtra(ReceiptSelect.TICKET_ID_KEY);
                    try {
                        getTicketFragment().onTicketRefund(getTicketFromTicketId(ticketId));
                    } catch (NotFoundException ignore) {
                    }
                }
                break;
            case COMPOSITION:
                if (resultCode == Activity.RESULT_OK) {
                    CompositionInstance compo = (CompositionInstance)
                            data.getSerializableExtra("composition");
                    addACompoToTicket(compo);
                }
                break;
            // TODO: TEST restaurant implementation.
            case RESTAURANT_TICKET_FINISH:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED:
                        // Back to start
                        finish();
                        break;
                    case Activity.RESULT_OK:
                        mPendingTicket = Data.Session.currentSession(mContext).getCurrentTicket();
                        mPager.setCurrentItem(CATALOG_FRAG);
                        break;
                }
                break;
            case SCALE_SELECT:
                if (resultCode == Activity.RESULT_OK) {
//                    scaleMacAddress = data.getStringExtra(BluetoothScaleSelectionActivity.);
//                    String scaleName = data.getStringExtra(BluetoothScaleSelectionActivity.EXTRA_SCALE_NAME);

//                    if (scaleManager != null && scaleMacAddress != null) {
//                        // Check if this is a virtual scale connection
//                        if (scaleMacAddress.equals("VIRTUAL:AC:LAS:SC:AL:E0")) {
//                            // Initialize virtual scale testing
//                          //  scaleManager.initializeVirtualScaleTesting();
//                            // Connect to virtual scale
//                         //   boolean connected = scaleManager.connectToVirtualScale();
//
//                            if (connected) {
//                                Log.d("Transaction", "Connected to virtual scale");
//                                Toast.makeText(this, "Connected to virtual scale", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e("Transaction", "Failed to connect to virtual scale");
//                                Toast.makeText(this, "Failed to connect to virtual scale", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            // Regular Bluetooth scale connection
//                       //     scaleManager.connectToScale(scaleMacAddress);
//                        }
//                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private Ticket getTicketFromTicketId(String ticketId) throws NotFoundException {
        List<Receipt> list = Data.Receipt.getReceipts(OrtusPOS.getAppContext());
        for (Receipt receipt : list) {
            Ticket ticket = receipt.getTicket();
            if (ticket.getId().equals(ticketId)) {
                return ticket;
            }
        }
        throw new NotFoundException();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }
    
    private String scaleMacAddress;

    private void initializeScaleManager() {
        scaleManager = new ScaleManager(this);
        scaleManager.setConnectionStateListener(new ScaleManager.ConnectionStateListener() {
            @Override
            public void onScaleConnected() {
                Log.d("Transaction", "Scale connected");
                runOnUiThread(() -> {
                    Toast.makeText(Transaction.this, "Scale connected", Toast.LENGTH_SHORT).show();
                    invalidateOptionsMenu();
                });
            }

            @Override
            public void onScaleDisconnected() {
                Log.d("Transaction", "Scale disconnected");
                runOnUiThread(() -> {
                    Toast.makeText(Transaction.this, "Scale disconnected", Toast.LENGTH_SHORT).show();
                    invalidateOptionsMenu();
                });
            }

            @Override
            public void onScaleError(String errorMessage) {
                Log.e("Transaction", "Scale error: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(Transaction.this, "Scale error: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean returnToCatalogueView() {
        if (getCatalogFragment().displayProducts()) {
            getCatalogFragment().setCategoriesVisible();
            return true;
        }
        return false;
    }

    /*
     *  INTERFACE
     */

    @Override
    public void onCfProductClicked(Product p, Catalog catData) {
        registerAProduct(p, catData);
    }

    @Override
    public boolean onCfProductLongClicked(final Product p) {
        // Set default line values
        TariffArea area = getTicketFragment().getTicketData().getTariffArea();
        TicketLine l = new TicketLine(p, 1, area);
        // Pass it to the edit dialog
        TicketLineEditDialog dial = TicketLineEditDialog.newInstance(l);
        dial.setDialogListener(this);
        dial.show(getSupportFragmentManager
                (), TicketLineEditDialog.TAG);
        return true;
    }

    @Override
    public void onTicketLineEdited(TicketLine line) {
        this.getTicketFragment().addLine(line);
        this.getTicketFragment().updateView();
    }

    @Override
    public void OnCfCatalogViewChanged(boolean catalogIsVisible, Category category) {
        if (catalogIsVisible) {
            this.setActionBarTitle(getString(R.string.catalog));
        } else if (category != null) {
            this.setActionBarTitle(category.getLabel());
        } else {
            this.setActionBarTitle(getString(R.string.no_category));
        }
        this.setActionBarHomeVisibility(!catalogIsVisible);
    }

    @Override
    public void onPsdPositiveClick(Product p, double weight, boolean isProductReturned) {
        Log.i("Transaction", "onPsdPositiveClick called - Product: " + p.getLabel() + 
                ", Weight: " + weight + ", IsReturn: " + isProductReturned);
        
        if (weight > 0) {
            Log.i("Transaction", "Adding scaled product to ticket - Weight: " + weight);
            if (isProductReturned) {
                addAScaledProductReturnToTicket(p, weight);
            } else {
                addAScaledProductToTicket(p, weight);
            }
        } else {
            Log.e("Transaction", "ERROR: Weight is zero or negative: " + weight);
        }
    }

    @Override
    public void onMidProductCreated(Product product) {
        addAProductToTicket(product);
    }

    @Override
    public void onMidProductPick(Product product) {
        CatalogFragment cat = getCatalogFragment();
        registerAProduct(product, cat.getCatalogData());
//        disposeCatalogFragment(cat);
    }

    @Override
    public void onTfCheckInClick() {
        mPager.setCurrentItem(CATALOG_FRAG);
    }

    @Override
    public void onTFTicketChanged() {
        updatePaymentFragment(getTicketFragment(), getPaymentFragment());
    }

    @Override
    public void onTfCheckOutClick() {
        mPager.setCurrentItem(PAYMENT_FRAG);
    }

    @Override
    public void onPfPrintReceipt(final Receipt receipt) {
        this.printReceipt(receipt);
    }

    @Override
    public void onPfCustomerListClick() {
        showCustomerList();
    }

    @Override
    public Receipt onPfSaveReceipt() {
        TicketFragment t = getTicketFragment();
        Ticket ticketData = t.getTicketData();
        ticketData.setTicketId(String.valueOf(Data.TicketId.newTicketId()));
        ticketData.setCashSession(Data.Cash.currentCash(mContext));
        // Create and save the receipt and remove from session
        Session currSession = Data.Session.currentSession(mContext);
        User user = currSession.getUser();
        final Receipt r = new Receipt(ticketData, ticketData.getPaymentsCollection(), user);
        if (Configure.getDiscount(mContext)) {
            r.setDiscount(Data.Discount.getADiscount());
        }
        Data.Receipt.addReceipt(r);
        Data.TicketId.ticketClosed(mContext);
        try {
            Data.Receipt.save();
        } catch (IOError e) {
            Log.e(LOG_TAG, "Unable to save receipts", e);
            Error.showError(R.string.err_save_receipts, this);
        }
        currSession.closeTicket(ticketData);
        try {
            Data.Session.save();
        } catch (IOError ioe) {
            Log.e(LOG_TAG, "Unable to save session", ioe);
            Error.showError(R.string.err_save_session, this);
        }
//        disposeTicketFragment(t);
        return r;
    }

    @Override
    public void onPfFinished() {
        PaymentFragment payment = getPaymentFragment();
        payment.resetPaymentList();
//        disposePaymentFragment(payment);
        Session currSession = Data.Session.currentSession(mContext);
        this.returnToCatalogueView();
        // Return to a new ticket edit
        switch (Configure.getTicketsMode(mContext)) {
            case Configure.STANDARD_MODE:
                if (!currSession.hasTicket()) {
                    mPendingTicket = currSession.newCurrentTicket();
                    mPager.setCurrentItem(CATALOG_FRAG);
                } else {
                    // Pick last ticket
                    currSession.setCurrentTicket(currSession.getTickets().get(currSession.getTickets().size() - 1));
                    mPendingTicket = currSession.getCurrentTicket();
                    mPager.setCurrentItem(CATALOG_FRAG);
                }
                break;
            case Configure.RESTAURANT_MODE:
                Intent i = new Intent(mContext, RestaurantTicketSelect.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(i, RESTAURANT_TICKET_FINISH);
                break;
        }
    }

    @Override
    public void onRequestAddPayment(Payment payment) {
        getTicketFragment().addPayment(payment);
        if (this.deviceManagerHasCashDrawer()) {
            this.openCashDrawer();
        }
    }

    @Override
    public void onRequestRemovePayment(Payment payment) {
        getTicketFragment().removePayment(payment);
    }

    @Override
    public void onCustomerPicked(Customer customer) {
        TicketFragment tFrag = getTicketFragment();
        tFrag.setCustomer(customer);
        tFrag.updateView();
        if (mPager.getCurrentItem() != CATALOG_FRAG) {
            updatePaymentFragment(tFrag, null);
        }
//        disposeTicketFragment(tFrag);
        try {
            Data.Session.save();
        } catch (IOError ioe) {
            Log.e(LOG_TAG, "Unable to save session", ioe);
            Error.showError(R.string.err_save_session, this);
        }
    }

    @Override
    public void onCustomerCreated(Customer customer) {
        if (Data.Customer.customers.size() == 1 && getSupportActionBar() != null) {
            invalidateOptionsMenu();
        }
        onCustomerPicked(customer);
    }


    private final ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case CATALOG_FRAG: {
                    TicketFragment ticket = getTicketFragment();
                    ticket.setState(TicketFragment.CHECKIN_STATE);
                    ticket.updatePageState();
                    if (mPendingTicket != null) {
                        ticket.switchTicket(mPendingTicket);
                        mPendingTicket = null;
                    }
//                    disposeTicketFragment(ticket);
                    invalidateOptionsMenu();
                    setActionBarTitleVisibility(true);
                    break;
                }
                case TICKET_FRAG:
                case PAYMENT_FRAG: {
                    TicketFragment t = getTicketFragment();
                    t.setState(TicketFragment.CHECKOUT_STATE);
                    t.updatePageState();
                    updatePaymentFragment(t, null);
                   // disposeTicketFragment(t);
                    invalidateOptionsMenu();
                    setActionBarTitleVisibility(false);
                    break;
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // If needed, keep logic here
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // If needed, keep logic here
        }
    };

//    @Override
//    public void onPageScrolled(int i, float v, int i1) {
//    }
//
//    @Override
//    public void onPageSelected(int i) {
//        switch (i) {
//            case CATALOG_FRAG: {
//                TicketFragment ticket = getTicketFragment();
//                ticket.setState(TicketFragment.CHECKIN_STATE);
//                ticket.updatePageState();
//                if (mPendingTicket != null) {
//                    ticket.switchTicket(mPendingTicket);
//                    mPendingTicket = null;
//                }
//                disposeTicketFragment(ticket);
//                invalidateOptionsMenu();
//                setActionBarTitleVisibility(true);
//                break;
//            }
//            case TICKET_FRAG:
//            case PAYMENT_FRAG: {
//                TicketFragment t = getTicketFragment();
//                t.setState(TicketFragment.CHECKOUT_STATE);
//                t.updatePageState();
//                updatePaymentFragment(t, null);
//                disposeTicketFragment(t);
//                invalidateOptionsMenu();
//                setActionBarTitleVisibility(false);
//                break;
//            }
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int i) {
//
//    }

    /*
     * ACTION MENU RELATED
     */

    private void setActionBarTitleVisibility(boolean visible) {
        if (toolbar != null) {
            if (visible) {
                toolbar.setTitle(getCurrentCategoryTitle());
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            } else {
                toolbar.setTitle("");
                toolbar.setNavigationIcon(null);
            }
        }
    }

    private void enableActionBarTitle() {
        if (toolbar != null) {
            toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
            toolbar.setTitle(getString(R.string.app_name));
        }
    }

    private void setActionBarHomeVisibility(boolean isVisible) {
        if (toolbar != null) {
            if (isVisible) {
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                toolbar.setNavigationOnClickListener(v -> returnToCatalogueView());
            } else {
                toolbar.setNavigationIcon(null);
            }
        }
    }

    private void setActionBarTitle(String value) {
        if (toolbar != null && value != null) {
            toolbar.setTitle(value);
        }
    }

    private String getCurrentCategoryTitle() {
        Category category = getCatalogFragment().getCurrentCategory();
        if (category != null) {
            return category.getLabel();
        }
        return getString(R.string.catalog);
    }

    private void cleanLastScanIfRequired() {
        long current = System.currentTimeMillis();
        if (current - this.lastBarCodeTime > Transaction.SCANNERTIMER) {
            this.barcode = "";
        }
        this.lastBarCodeTime = current;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.cleanLastScanIfRequired();
        if (this.barcode == null) {
            this.barcode = "";
        }
        this.barcode += event.getNumber();
        if (this.barcode.length() == 13) {
            Log.i("keyboard", this.barcode);
            if (BarcodeCheck.ean13(this.barcode)) {
                this.readBarcode(this.barcode);
            } else {
                Toast.makeText(this, getString(R.string.err_wrong_ean13), Toast.LENGTH_SHORT).show();
            }
            this.barcode = "";
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ab_ticket_input, menu);

        if (Data.Customer.customers.isEmpty()) {
            menu.findItem(R.id.ab_menu_customer_list).setEnabled(false);
        }

        User cashier = Data.Session.currentSession(mContext).getUser();
        if (cashier.hasPermission("com.opurex.ortus.pos.panels.JPanelCloseMoney")) {
            menu.findItem(R.id.ab_menu_close_session).setEnabled(true);
        }

        getMenuInflater().inflate(R.menu.ab_scale_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!Data.Receipt.hasReceipts()
                || !Data.Session.currentSession(mContext).getUser().hasPermission("sales.EditTicket")) {
            menu.findItem(R.id.ab_menu_past_ticket).setVisible(false);
        }

        int currentItem = mPager.getCurrentItem();

        if (currentItem != CATALOG_FRAG) {
            menu.findItem(R.id.ab_menu_manual_input).setEnabled(false);
        }

        if (!deviceManagerHasCashDrawer()) {
            menu.findItem(R.id.ab_menu_cashdrawer).setVisible(false);
        }

        OpurexConfiguration conf = OrtusPOS.getConfiguration();
        menu.findItem(R.id.ab_menu_printmain).setVisible(!OpurexConfiguration.PrinterDriver.NONE.equals(conf.getPrinterDriver(0)));
        menu.findItem(R.id.ab_menu_printaux1).setVisible(!OpurexConfiguration.PrinterDriver.NONE.equals(conf.getPrinterDriver(1)));
        menu.findItem(R.id.ab_menu_printaux2).setVisible(!OpurexConfiguration.PrinterDriver.NONE.equals(conf.getPrinterDriver(2)));

        if (currentItem != TICKET_FRAG) {
            menu.findItem(R.id.ab_menu_divider).setEnabled(true);
        }

        MenuItem connectItem = menu.findItem(R.id.ab_menu_scale_connect);
//        MenuItem disconnectItem = menu.findItem(R.id.ab_menu_scale_disconnect);
//        if (scaleManager != null && scaleManager.isConnected()) {
//            connectItem.setVisible(false);
//            disconnectItem.setVisible(true);
//        } else {
//            connectItem.setVisible(true);
//            disconnectItem.setVisible(false);
//        }

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                returnToCatalogueView();
//                return true;
//            case R.id.ab_menu_divider:
//                startDivider();
//                return true;
//            case R.id.ab_menu_cashdrawer:
//                openCashDrawer();
//                return true;
//            case R.id.ab_menu_printmain:
//                return printTicket(0);
//            case R.id.ab_menu_printaux1:
//                return printTicket(1);
//            case R.id.ab_menu_printaux2:
//                return printTicket(2);
//            case R.id.ab_menu_manual_input:
//                new ManualInputDialog().show(getSupportFragmentManager(), ManualInputDialog.TAG);
//                return true;
//            case R.id.ab_menu_customer_list:
//                showCustomerList();
//                return true;
//            case R.id.ab_menu_customer_add:
//                createNewCustomer();
//                return true;
//            case R.id.ab_menu_calendar:
//                openCalendarApp();
//                return true;
//            case R.id.ab_menu_past_ticket:
//                startActivityForResult(new Intent(mContext, ReceiptSelect.class), PAST_TICKET_FOR_RESULT);
//                return true;
//            case R.id.ab_menu_close_session:
//                CloseCash.close(this);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.ab_ticket_input, menu);
//
//        if (Data.Customer.customers.size() == 0) {
//            menu.findItem(R.id.ab_menu_customer_list).setEnabled(false);
//        }
//        User cashier = Data.Session.currentSession(mContext).getUser();
//        if (cashier.hasPermission("com.opurex.ortus.pos.panels.JPanelCloseMoney")) {
//            menu.findItem(R.id.ab_menu_close_session).setEnabled(true);
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (!Data.Receipt.hasReceipts()
//                || !Data.Session.currentSession(mContext).getUser().hasPermission("sales.EditTicket")) {
//            menu.findItem(R.id.ab_menu_past_ticket).setVisible(false);
//        }
//        int currentItem = mPager.getCurrentItem();
//        if (currentItem != CATALOG_FRAG) {
//            menu.findItem(R.id.ab_menu_manual_input).setEnabled(false);
//        }
//        if (!deviceManagerHasCashDrawer()) {
//            menu.findItem(R.id.ab_menu_cashdrawer).setVisible(false);
//        }
//        OpurexConfiguration conf = OrtusPOS.getConfiguration();
//        menu.findItem(R.id.ab_menu_printmain).setVisible(!OpurexConfiguration.PrinterDriver.NONE.equals(conf.getPrinterDriver(0)));
//        menu.findItem(R.id.ab_menu_printaux1).setVisible(!OpurexConfiguration.PrinterDriver.NONE.equals(conf.getPrinterDriver(1)));
//        menu.findItem(R.id.ab_menu_printaux2).setVisible(!OpurexConfiguration.PrinterDriver.NONE.equals(conf.getPrinterDriver(2)));
//        if (currentItem != TICKET_FRAG) {
//            //TODO: should be false
//            //Debug mode
//            menu.findItem(R.id.ab_menu_divider).setEnabled(true);
//        }
//        return true;
//    }
//
        private static final int SCALE_SELECT = 5;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.returnToCatalogueView();
                break;
            case R.id.ab_menu_divider:
                this.startDivider();
                break;
            case R.id.ab_menu_cashdrawer:
                //TODO: clean this out by displaying Dialog if issue
                this.openCashDrawer();
                break;
            case R.id.ab_menu_printmain:
                TicketFragment t = getTicketFragment();
                Ticket tkt = t.getTicketData();
                if (!this.printOrder(0, tkt)) {
                    Toast.makeText(this, getString(R.string.print_no_connexion), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ab_menu_printaux1:
                t = getTicketFragment();
                tkt = t.getTicketData();
                if (!this.printOrder(1, tkt)) {
                    Toast.makeText(this, getString(R.string.print_no_connexion), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ab_menu_printaux2:
                t = getTicketFragment();
                tkt = t.getTicketData();
                if (!this.printOrder(2, tkt)) {
                    Toast.makeText(this, getString(R.string.print_no_connexion), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ab_menu_manual_input:
                DialogFragment dial = new ManualInputDialog();
                dial.show(getSupportFragmentManager
                        (), ManualInputDialog.TAG);
                break;
            case R.id.ab_menu_customer_list:
                showCustomerList();
                break;
            case R.id.ab_menu_customer_add:
                createNewCustomer();
                break;
            case R.id.ab_menu_calendar:
                java.util.Calendar starTime = Calendar.getInstance();

                Uri uri = Uri.parse("content://com.android.calendar/time/" +
                        String.valueOf(starTime.getTimeInMillis()));

                Intent openCalendar = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openCalendar);
                break;
            case R.id.ab_menu_past_ticket:
                Intent receiptSelect = new Intent(mContext, ReceiptSelect.class);
                this.startActivityForResult(receiptSelect, PAST_TICKET_FOR_RESULT);
                break;
            case R.id.ab_menu_close_session:
                CloseCash.close(this);
                break;
            case R.id.ab_menu_scale_connect:
                Intent intent = new Intent(this,BluetoothScaleSelectionActivity.class);
                startActivityForResult(intent, SCALE_SELECT);
                return true;
            case R.id.ab_menu_send_p2p:
                this.sendTicketToMaster();
                return true;
//                return true;
            default:
                return false;

            /*
                case MENU_BARCODE:
                scanBarcode(nulClabil);
                break;
            */
        }
        return true;
    }

    private void sendTicketToMaster() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String devices = prefs.getString("p2p_devices", "");
        if (devices.isEmpty()) {
            Toast.makeText(this, "No master device added. Please scan and add a device first.", Toast.LENGTH_LONG).show();
            return;
        }

        String[] addresses = devices.split(",");
        String targetAddress = addresses[0]; // Send to first added device

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = adapter.getRemoteDevice(targetAddress);

        new Thread(() -> {
            try {
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                socket.connect();
                P2PPaymentService.sendTicket(socket, Data.Session.currentSession().getCurrentTicket());
                runOnUiThread(() -> Toast.makeText(this, "Ticket sent to " + targetAddress, Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error connecting to master device", e);
                runOnUiThread(() -> Toast.makeText(this, "Failed to send ticket: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void startDivider() {
        DividerDialog divider = DividerDialog.newInstance(Data.Session.currentSession().getCurrentTicket(), this);
//        divider.show(getSupportFragmentManager(), DividerDialog.TAG);
        divider.show(getFragmentManager(), DividerDialog.TAG);
    }

    @Override
    public DividerDialog.ResultListener onDividerDialogRequestResultListener() {
        return this;
    }

    @Override
    public void onDividerDialogResult(Ticket originalTicket, Ticket createdTicket) {
        if (createdTicket.isEmpty()) {
            return; // Nothing done
        }
        Session session = Data.Session.currentSession();
        // Update the original ticket
        Ticket current = session.getCurrentTicket();
        current.replaceLines(originalTicket.getLines());
        // Switch to the divided ticket
        session.addTicketToRunningTickets(createdTicket);
        getTicketFragment().switchTicket(createdTicket);
    }

    /*
     *  PRIVATES
     */

    // CUSTOMER RELATED FUNCTIONS

    private void createNewCustomer() {
        customerInfoDialog = CustomerInfoDialog.newInstance(true, null);

        customerInfoDialog.show(getSupportFragmentManager());
    }

    private void showCustomerList() {
        TicketFragment t = getTicketFragment();
        boolean bSetup = t.getCustomer() != null;
//        disposeTicketFragment(t);
        CustomerSelectDialog dialog = CustomerSelectDialog.newInstance(bSetup);
        dialog.setDialogListener(this);
        dialog.show(getSupportFragmentManager(), CustomerSelectDialog.TAG);
    }

    // PRODUCT RELATED FUNCTIONS

    /**
     * Asks for complementary product information before adding it to ticket
     *
     * @param p       Product to be added
     * @param catData The current Catalog for data comparison
     */
    private void registerAProduct(Product p, Catalog catData) {
        // TODO: COMPOSITION NOT TESTED
        if (Data.Composition.isComposition(p)) {
            Intent i = new Intent(mContext, CompositionInput.class);
            CompositionInput.setup(catData, Data.Composition.getComposition(p.getId()));
            startActivityForResult(i, COMPOSITION);
        } else if (p.isScaled()) {
            askForAScaledProduct(p, false);
        } else {
            addAProductToTicket(p);
        }
    }

    private void registerAProductReturn(Product p) {
        if (Data.Composition.isComposition(p)) {
            Toast.makeText(this, getString(R.string.refund_composition), Toast.LENGTH_LONG).show();
        } else if (p.isScaled()) {
            askForAScaledProduct(p, true);
        } else {
            addAProductReturnToTicket(p);
        }
    }

    void askForAScaledProduct(Product p, boolean isReturnProduct) {
        // If the product is scaled, asks the weight
        ProductScaleDialog dial = ProductScaleDialog.newInstance(p, isReturnProduct);
        dial.setDialogListener(this);
        dial.show(getSupportFragmentManager(), ProductScaleDialog.TAG);
    }

    // Only suitable for adding one product at a time because updateView is heavy
    private void addAProductToTicket(Product p) {
        TicketFragment ticket = getTicketFragment();
        ticket.scrollTo(ticket.addProduct(p));
        ticket.updateView();
//        disposeTicketFragment(ticket);
    }

    private void addACompoToTicket(CompositionInstance compo) {
        TicketFragment ticket = getTicketFragment();
        ticket.addProduct(compo);
        ticket.updateView();
//        disposeTicketFragment(ticket);
    }

    @Override
    public void addAScaledProductToTicket(Product p, double weight) {
        TicketFragment ticket = getTicketFragment();
        ticket.addScaledProduct(p, weight);
        ticket.scrollDown();
        ticket.updateView();
//        disposeTicketFragment(ticket);
    }

    private void addAProductReturnToTicket(Product p) {
        TicketFragment ticket = getTicketFragment();
        ticket.scrollTo(ticket.addProductReturn(p));
        ticket.updateView();
//        disposeTicketFragment(ticket);
    }

    @Override
    public void addAScaledProductReturnToTicket(Product p, double weight) {
        TicketFragment ticket = getTicketFragment();
        ticket.addScaledProductReturn(p, weight);
        ticket.scrollDown();
        ticket.updateView();
//        disposeTicketFragment(ticket);
    }

    private void readBarcode(String code) {
        // It is a DISCOUNT Barcode
        if (code.startsWith(Barcode.Prefix.DISCOUNT)) {
            try {
                Discount disc = Data.Discount.findFromBarcode(code);
                if (disc.isValid()) {
                    TicketFragment ticketFragment = getTicketFragment();
                    ticketFragment.setDiscountRate(disc.getRate());
                    ticketFragment.updateView();
//                    disposeTicketFragment(ticketFragment);
                    Log.i(LOG_TAG, "Discount: " + disc.getTitle(this.getApplicationContext()) + ", added");
                } else {
                    Toast.makeText(mContext, getString(R.string.discount_outdated), Toast.LENGTH_LONG).show();
                }
            } catch (NotFoundException e) {
                Log.e(LOG_TAG, "Discount not found", e);
            }
            // Can not be something else
            return;
        }

        // Is it a customer card ?
        for (Customer c : Data.Customer.customers) {
            if (code.equals(c.getCard())) {
                onCustomerPicked(c);
                return;
            }
        }
        // Is it a product ?
        Catalog cat = Data.Catalog.catalog(mContext);
        Product p = cat.getProductByBarcode(code);
        if (p != null) {
            CatalogFragment catFrag = getCatalogFragment();
            registerAProduct(p, catFrag.getCatalogData());
//            disposeCatalogFragment(catFrag);
            String text = getString(R.string.barcode_found, p.getLabel());
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            return;
        }

        // Nothing found
        String text = getString(R.string.barcode_not_found, code);
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    //  FRAGMENT RELATED FUNCTIONS

    /**
     * To be used with dispose function
     * i.e:    SomeFragment sFrag = getSomeFragment();
     * // Code using sFrag
     * disposeSomeFragment(sFrag);
     */
//    private CatalogFragment getCatalogFragment() {
//        return (CatalogFragment) mPagerAdapter.getFragment(mPager, CATALOG_FRAG); Expected 3 arguments but found 2
//    }
//
//    private TicketFragment getTicketFragment() {
//        return (TicketFragment) mPagerAdapter.getFragment(mPager, TICKET_FRAG);Expected 3 arguments but found 2
//    }
//
//    private PaymentFragment getPaymentFragment() {
//        return (PaymentFragment) mPagerAdapter.getFragment(mPager, PAYMENT_FRAG);Expected 3 arguments but found 2
//    }
    private CatalogFragment getCatalogFragment() {
        TransactionMainFragment mainFrag = mPagerAdapter.getFragment(0, TransactionMainFragment.class);
        return (mainFrag != null) ? mainFrag.getCatalogFragment() : null;
    }

    private TicketFragment getTicketFragment() {
        TransactionMainFragment mainFrag = mPagerAdapter.getFragment(0, TransactionMainFragment.class);
        return (mainFrag != null) ? mainFrag.getTicketFragment() : null;
    }

//    private PaymentFragment getPaymentFragment() {
//        return mPagerAdapter.getFragment(1, PaymentFragment.class);
//    }

    private PaymentFragment getPaymentFragment() {
        Fragment parent = getSupportFragmentManager().findFragmentByTag("f1");
        if (parent instanceof PaymentMainFragment) {
            return ((PaymentMainFragment) parent).getPaymentFragment();
        }
        return null;
    }




//    private void disposeCatalogFragment(CatalogFragment frag) {
//        mPagerAdapter.destroyForcedItem(mPager, CATALOG_FRAG, frag); Cannot resolve method 'destroyForcedItem' in 'TransactionPagerAdapter'
//    }
//
//    private void disposeTicketFragment(TicketFragment frag) {
//        mPagerAdapter.destroyForcedItem(mPager, TICKET_FRAG, frag);Cannot resolve method 'destroyForcedItem' in 'TransactionPagerAdapter'
//    }
//
//    private void disposePaymentFragment(PaymentFragment frag) {
//        mPagerAdapter.destroyForcedItem(mPager, PAYMENT_FRAG, frag);Cannot resolve method 'destroyForcedItem' in 'TransactionPagerAdapter'
//    }

    private void updatePaymentFragment(TicketFragment t, PaymentFragment p) {
        boolean bDisposeTicket = false;
        boolean bDisposePayment = false;
        if (t == null) {
            t = getTicketFragment();
            bDisposeTicket = true;
        }
        if (p == null) {
            p = getPaymentFragment();
            bDisposePayment = true;
        }
        p.setCurrentCustomer(t.getCustomer());
        p.setPaymentsList(t.getPaymentList());
        p.setTotalPrice(t.getTicketPrice());
        p.setTicketPrepaid(t.getTicketPrepaid());
        p.resetInput();
        p.updateView();
//        if (bDisposeTicket) disposeTicketFragment(t); // If layout is accepted per android doc
//        if (bDisposePayment) disposePaymentFragment(p);
    }

    /*
     *  ADAPTERS
     */


//    private class TransactionPagerAdapter extends FragmentStatePagerAdapter {
//        private final SparseArray<Fragment> mFragmentReferences = new SparseArray<>();
//        private final SparseBooleanArray mWasForced = new SparseBooleanArray();
//
//        public TransactionPagerAdapter(@NonNull FragmentManager fm) {
//            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            Class<? extends ViewPageFragment> cls = PAGES[position].getPageFragment();
//            try {
//                Method newInstance = cls.getMethod("newInstance", int.class);
//                return (Fragment) newInstance.invoke(null, position);
//            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
//                throw new RuntimeException("Failed to instantiate fragment: " + cls.getName(), e);
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return PAGES.length;
//        }
//
//        // Removed @Override — this method is not defined in superclass
//        public float getPageWidth(int position) {
//            return PAGES[position].getWidth();
//        }
//
//        @Override
//        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            Fragment fragment = (Fragment) super.instantiateItem(container, position);
//            mFragmentReferences.put(position, fragment);
//            mWasForced.put(position, false);
//            return fragment;
//        }
//
//        @Override
//        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            mFragmentReferences.remove(position);
//            mWasForced.delete(position);
//            super.destroyItem(container, position, object);
//        }
//
//        public <T extends Fragment> T getFragment(ViewGroup container, int position, Class<T> clazz) {
//            Fragment frag = mFragmentReferences.get(position);
//            if (frag == null) {
//                frag = (Fragment) instantiateItem(container, position);
//                mWasForced.put(position, true);
//            }
//            if (clazz.isInstance(frag)) {
//                return clazz.cast(frag);
//            }
//            return null;
//        }
//
//        public void destroyForcedItem(ViewGroup container, int position, Object object) {
//            if (mWasForced.get(position, false)) {
//                destroyItem(container, position, object);
//            }
//        }
//    }

//    private class TransactionPagerAdapter extends FragmentStatePagerAdapter {
//        SparseArray<Fragment> mFragmentReferences;
//        SparseBooleanArray mWasForced;
//
//        public TransactionPagerAdapter(FragmentManager fm) {
//            super(fm);
//            mFragmentReferences = new SparseArray<Fragment>();
//            mWasForced = new SparseBooleanArray();
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Class<? extends ViewPageFragment> cls = PAGES[position].getPageFragment();
//            ViewPageFragment result;
//            try {
//                Method newInstance = cls.getMethod("newInstance", int.class);
//                result = (ViewPageFragment) newInstance.invoke(null, position);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                throw new RuntimeException(cls.getName() +
//                        " must implement static newInstance function");
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e.getMessage());
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e.getMessage());
//            }
//            return result;
//        }
//
//        @Override
//        public int getCount() {
//            return PAGES.length;
//        }
//
//        @Override
//        public float getPageWidth(int position) {
//            return PAGES[position].getWidth();
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            Fragment fragment = (Fragment) super.instantiateItem(container, position);
//            mFragmentReferences.put(position, fragment);
//            mWasForced.put(position, false);
//            return fragment;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            mFragmentReferences.delete(position);
//            mWasForced.delete(position);
//            super.destroyItem(container, position, object);
//        }
//
//        /*
//         *  The ViewPager handles it's own fragment lifecycle
//         *  and FragmentStatePagerAdapter automatically saves fragment states upon deletion.
//         *  The getFragment method force the fragment instance to be able to manipulate it
//         *  even when it's outside of the view + page limit.
//         */
//        public Fragment getFragment(ViewGroup container, int position) {
//            Fragment frag = mFragmentReferences.get(position, null);
//            if (frag == null) {
//                frag = (Fragment) instantiateItem(container, position);
//                mWasForced.put(position, true);
//            }
//            return frag;
//        }
//
//        /*
//         *  This method should be called at the end of any method using getFragment as
//         *  a C++ destructor to optimise memory consumption.
//         */
//        public void destroyForcedItem(ViewGroup container, int position, Object object) {
//            if (mWasForced.get(position, false)) {
//                destroyItem(container, position, object);
//            }
//        }
//    }

    /*
     *  CALLBACK
     */

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(p2pReceiver);
        if (mPager != null) {
            mPager.unregisterOnPageChangeCallback(pageChangeCallback);
        }
        
        // Clean up the scale manager
//        if (scaleManager != null) {
//            scaleManager.cleanup();
//            scaleManager = null;
//        }
    }


    @Override
    public void onDeviceManagerEvent(final POSDeviceManager manager, final DeviceManagerEvent event) {
        Transaction.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (event.what) {
                    case DeviceManagerEvent.BaseRotation:
                        Transaction.this.onBaseRotation(event);
                        break;
                    case DeviceManagerEvent.ScannerReader:
                        Transaction.this.readBarcode(event.getString());
                        break;
                    case DeviceManagerEvent.PrintQueued:
                        Object extra = event.getExtra();
                        if (extra == null) {
                            break;
                        }
                        if (extra instanceof ReceiptDocument) {
                            Toast.makeText(Transaction.this, getString(R.string.print_ticket_queued),
                                    Toast.LENGTH_LONG).show();
                        } else if (extra instanceof OrderDocument) {
                            Toast.makeText(Transaction.this, getString(R.string.print_order_queued),
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case DeviceManagerEvent.PrintError:
                        Toast.makeText(Transaction.this, getString(R.string.print_no_connexion), Toast.LENGTH_LONG).show();
                        break;
                    case DeviceManagerEvent.PrintDone:
                        extra = event.getExtra();
                        if (extra == null) {
                            break;
                        }
                        if ((extra instanceof ReceiptDocument) || (extra instanceof OrderDocument)) {
                            Toast.makeText(Transaction.this, getString(R.string.print_done),
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    private void onBaseRotation(DeviceManagerEvent event) {
        if (event.extraEquals(PowaPOSEnums.RotationSensorStatus.ROTATED)) {
            createNewCustomer();
        }
    }
}
