/*
    Opurex Android com.opurex.ortus.client
    Copyright (C) Opurex contributors, see the COPYRIGHT file

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.opurex.ortus.client.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opurex.ortus.client.data.Data;
import com.opurex.ortus.client.models.Stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class for handling stock synchronization with the web backend.
 */
public class StockSyncService {
    
    private static final String TAG = "StockSyncService";
    private static final String API_BASE_URL = "http://your-server-address/api"; // TODO: Replace with actual server address
    
    private Context context;
    private ExecutorService executor;
    private Gson gson;
    
    public StockSyncService(Context context) {
        this.context = context;
        this.executor = Executors.newFixedThreadPool(3);
        this.gson = new Gson();
    }
    
    /**
     * Get stock level for a product from the web backend
     * @param productId The product ID
     * @param callback Callback to handle the result
     */
    public void getStockFromServer(String productId, StockCallback callback) {
        executor.submit(() -> {
            try {
                String urlString = API_BASE_URL + "/stock/" + productId;
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    String status = jsonResponse.get("status").getAsString();
                    
                    if ("success".equals(status)) {
                        JsonObject data = jsonResponse.getAsJsonObject("data");
                        Stock stock = gson.fromJson(data, Stock.class);
                        callback.onSuccess(stock);
                    } else {
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown error";
                        callback.onError(message);
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }
                
                connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error getting stock from server", e);
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Update stock level for a product on the web backend
     * @param productId The product ID
     * @param stock The stock object to update
     * @param callback Callback to handle the result
     */
    public void updateStockOnServer(String productId, Stock stock, StockCallback callback) {
        executor.submit(() -> {
            try {
                String urlString = API_BASE_URL + "/stock/" + productId;
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                
                // Prepare request body
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("quantity", stock.getQuantity());
                requestBody.addProperty("security_level", stock.getSecurity());
                requestBody.addProperty("max_level", stock.getMaxLevel());
                                requestBody.addProperty("user_id", Integer.parseInt(Data.User.users(context).get(0).getId()));
                
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.toString().getBytes());
                outputStream.flush();
                outputStream.close();
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    String status = jsonResponse.get("status").getAsString();
                    
                    if ("success".equals(status)) {
                        callback.onSuccess(stock);
                    } else {
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown error";
                        callback.onError(message);
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }
                
                connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error updating stock on server", e);
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Add stock to a product on the web backend
     * @param productId The product ID
     * @param quantity The quantity to add
     * @param reason The reason for the addition
     * @param reference Optional reference
     * @param callback Callback to handle the result
     */
    public void addStockOnServer(String productId, double quantity, String reason, String reference, StockCallback callback) {
        executor.submit(() -> {
            try {
                String urlString = API_BASE_URL + "/stock/" + productId + "/add";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                
                // Prepare request body
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("quantity", quantity);
                requestBody.addProperty("reason", reason);
                requestBody.addProperty("reference", reference);
                                requestBody.addProperty("user_id", Integer.parseInt(Data.User.users(context).get(0).getId()));
                
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.toString().getBytes());
                outputStream.flush();
                outputStream.close();
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    String status = jsonResponse.get("status").getAsString();
                    
                    if ("success".equals(status)) {
                        // Get updated stock
                        getStockFromServer(productId, callback);
                    } else {
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown error";
                        callback.onError(message);
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }
                
                connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error adding stock on server", e);
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Remove stock from a product on the web backend
     * @param productId The product ID
     * @param quantity The quantity to remove
     * @param reason The reason for the removal
     * @param reference Optional reference
     * @param callback Callback to handle the result
     */
    public void removeStockOnServer(String productId, double quantity, String reason, String reference, StockCallback callback) {
        executor.submit(() -> {
            try {
                String urlString = API_BASE_URL + "/stock/" + productId + "/remove";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                
                // Prepare request body
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("quantity", quantity);
                requestBody.addProperty("reason", reason);
                requestBody.addProperty("reference", reference);
                                requestBody.addProperty("user_id", Integer.parseInt(Data.User.users(context).get(0).getId()));
                
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.toString().getBytes());
                outputStream.flush();
                outputStream.close();
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    String status = jsonResponse.get("status").getAsString();
                    
                    if ("success".equals(status)) {
                        // Get updated stock
                        getStockFromServer(productId, callback);
                    } else {
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown error";
                        callback.onError(message);
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }
                
                connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error removing stock on server", e);
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Get all products with low stock from the web backend
     * @param callback Callback to handle the result
     */
    public void getLowStockProductsFromServer(LowStockCallback callback) {
        executor.submit(() -> {
            try {
                String urlString = API_BASE_URL + "/stock/low";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    String status = jsonResponse.get("status").getAsString();
                    
                    if ("success".equals(status)) {
                        String[] productIds = gson.fromJson(jsonResponse.getAsJsonArray("data"), String[].class);
                        callback.onSuccess(productIds);
                    } else {
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown error";
                        callback.onError(message);
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }
                
                connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error getting low stock products from server", e);
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Callback interface for stock operations
     */
    public interface StockCallback {
        void onSuccess(Stock stock);
        void onError(String error);
    }
    
    /**
     * Callback interface for low stock operations
     */
    public interface LowStockCallback {
        void onSuccess(String[] productIds);
        void onError(String error);
    }
}