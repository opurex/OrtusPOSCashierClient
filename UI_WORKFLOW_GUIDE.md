# UI Workflow Guide for Scale Integration

This guide explains how the UI, specifically a `TransactionFragment` and the `ProductScaleDialog`, should interact with the `ScaleManager` to handle the sale of a scaled product.

## The Workflow

The process involves three main components:
1.  **`TransactionFragment`**: The screen where the user selects products to add to a ticket.
2.  **`ProductScaleDialog`**: The dialog that appears to capture the weight of a scaled product.
3.  **`ScaleManager`**: The backend service that communicates with the Bluetooth scale.

---

### Step 1: Product Selection in `TransactionFragment`

When a user taps on a product in your product list, you need to check if it's a scaled product.

- **If `product.isScaled()` is `false`**: Add the product to the ticket as usual.
- **If `product.isScaled()` is `true`**: Do **not** add it to the ticket yet. Instead, open the `ProductScaleDialog` and pass the product information to it.

The `TransactionFragment` needs to implement a listener to get the final weight back from the dialog.

```java
// In TransactionFragment.java

public class TransactionFragment extends Fragment implements ProductScaleDialog.OnWeightConfirmedListener {

    private Ticket currentTicket;
    private Product selectedProduct;

    // This is your product click handler
    private void onProductSelected(Product product) {
        if (product.isScaled()) {
            this.selectedProduct = product;
            ProductScaleDialog dialog = ProductScaleDialog.newInstance(product);
            dialog.setTargetFragment(this, 0); // Set this fragment as the listener
            dialog.show(getParentFragmentManager(), "ProductScaleDialog");
        } else {
            currentTicket.addProduct(product);
            updateTicketUI();
        }
    }

    // This callback is triggered when the user confirms the weight in the dialog
    @Override
    public void onWeightConfirmed(double weight) {
        if (selectedProduct != null && weight > 0) {
            // Now add the scaled product to the ticket with the confirmed weight
            currentTicket.addScaledProduct(selectedProduct, weight);
            updateTicketUI();
        }
    }

    private void updateTicketUI() {
        // Your code to refresh the ticket display
    }
}
```

---

### Step 2: `ProductScaleDialog` Interaction with `ScaleManager`

The `ProductScaleDialog` is the central hub for scale interaction. It gets a `ScaleManager` instance and uses it to update its UI in real-time.

```java
// In ProductScaleDialog.java

public class ProductScaleDialog extends DialogFragment implements ScaleManager.ScaleWeightListener, ScaleManager.ConnectionStateListener {

    // UI Elements
    private TextView weightTextView;
    private TextView priceTextView;
    private TextView connectionStatusTextView;
    private Button zeroButton, tareButton, confirmButton;

    private ScaleManager scaleManager;
    private Product product;
    private double currentWeight = 0.0;

    // Listener to send the final weight back to the fragment
    public interface OnWeightConfirmedListener {
        void onWeightConfirmed(double weight);
    }
    private OnWeightConfirmedListener listener;

    public static ProductScaleDialog newInstance(Product product) {
        ProductScaleDialog dialog = new ProductScaleDialog();
        // Pass product data to the dialog
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.product = (Product) getArguments().getSerializable("product");
        
        // Get a singleton instance of your ScaleManager
        this.scaleManager = new ScaleManager(getContext()); // Or get singleton instance
        
        // Attach the listener from the target fragment
        try {
            this.listener = (OnWeightConfirmedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnWeightConfirmedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register this dialog to receive live updates from the ScaleManager
        scaleManager.setScaleWeightListener(this);
        scaleManager.setConnectionStateListener(this);
        updateConnectionStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
        // IMPORTANT: Unregister to prevent memory leaks
        scaleManager.setScaleWeightListener(null);
        scaleManager.setConnectionStateListener(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ... inflate your layout and initialize views ...

        confirmButton.setOnClickListener(v -> {
            // Send the confirmed weight back to the fragment
            if (listener != null) {
                listener.onWeightConfirmed(this.currentWeight);
            }
            dismiss();
        });

        zeroButton.setOnClickListener(v -> scaleManager.zeroScale());
        tareButton.setOnClickListener(v -> scaleManager.tareScale());
        
        // You would also have buttons to trigger scaleManager.startScan() and connect()

        return view;
    }

    // --- Callbacks from ScaleManager ---

    @Override
    public void onWeightReceived(double weight, String unit) {
        getActivity().runOnUiThread(() -> {
            this.currentWeight = weight;
            weightTextView.setText(String.format("%.3f %s", weight, unit));
            // Real-time price calculation
            double totalPrice = product.getPrice() * weight;
            priceTextView.setText(String.format("Total: $%.2f", totalPrice));
        });
    }

    @Override
    public void onScaleConnected() {
        getActivity().runOnUiThread(() -> connectionStatusTextView.setText("Connected"));
    }

    @Override
    public void onScaleDisconnected() {
        getActivity().runOnUiThread(() -> connectionStatusTextView.setText("Disconnected"));
    }

    @Override
    public void onScaleError(String errorMessage) {
        getActivity().runOnUiThread(() -> connectionStatusTextView.setText("Error: " + errorMessage));
    }
}
```

---

### Step 3: Completing the Transaction

Once the `ProductScaleDialog` dismisses, the `onWeightConfirmed` method in your `TransactionFragment` is called. This method takes the final weight and uses it to add the scaled product to the ticket, completing the workflow.
