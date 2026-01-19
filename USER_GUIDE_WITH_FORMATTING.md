# OrtusPOS Scale Integration User Guide

## Table of Contents
1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
3. [Connecting to a Real Aclas Scale](#connecting-to-a-real-aclass-scale)
4. [Using the Scale for Scaled Products](#using-the-scale-for-scaled-products)
5. [Tare and Zero Functions](#tare-and-zero-functions)
6. [Manual Weight Entry](#manual-weight-entry)
7. [Troubleshooting](#troubleshooting)
8. [Settings Configuration](#settings-configuration)

---

## 1. Introduction

The OrtusPOS system integrates with Aclas Bluetooth scales to provide accurate weight measurements for products sold by weight. This guide explains how to use the scale integration for optimal retail operations.

### What You Can Do With This System
- Connect to real Aclas Bluetooth scales
- Automatically capture weight measurements
- Calculate prices based on weight
- Use tare and zero functions for container weight removal
- Enter weights manually when needed
- Test with virtual scale functionality

---

## 2. Getting Started

### Prerequisites Checklist
□ Aclas Bluetooth scale (powered on and ready to pair)  
□ OrtusPOS app version 2.24 installed  
□ Bluetooth enabled on POS device  
□ Scaled products configured in your database  

### Understanding Scaled Products
Products sold by weight require the system to know the exact weight to calculate the price. Common examples include:
- Fresh produce (fruits and vegetables)
- Meat and poultry products
- Deli items and prepared foods
- Bulk items and packaged goods priced per kilogram

---

## 3. Connecting to a Real Aclas Scale

### Step-by-Step Connection Process

┌─────────────────────────────────────────────────────────────┐
│                    CONNECTION PROCESS                       │
├─────────────────────────────────────────────────────────────┤
│ 1. Open OrtusPOS app                                      │
│ 2. Go to Settings → Scale Configuration                   │
│ 3. Select "Scan for devices"                              │
│ 4. Wait for scale discovery                               │
│ 5. Select your Aclas scale from the list                  │
│ 6. Verify connection status shows "Scale connected"       │
└─────────────────────────────────────────────────────────────┘

### Detailed Steps

**Step 1: Prepare Your Scale**
- Turn on your Aclas Bluetooth scale
- Put the scale in pairing mode (refer to your scale manual)
- Ensure the scale is within range of your POS device

**Step 2: Access Scale Selection**
- Open the OrtusPOS app
- Navigate to Settings
- Select "Scale Configuration"
- Tap "Scan for devices"

**Step 3: Connect to Your Scale**
- Wait for the scan to complete
- Locate your Aclas scale in the device list
- Tap on your scale to establish connection
- Wait for confirmation of successful connection

**Step 4: Verify Connection**
- Look for "Scale connected" message in green text
- Check that weight readings appear when items are placed on the scale
- Confirm that the weight display updates in real-time

---

## 4. Using the Scale for Scaled Products

### Complete Workflow

┌─────────────────────────────────────────────────────────────┐
│                SCALED PRODUCT WORKFLOW                      │
├─────────────────────────────────────────────────────────────┤
│ SELECT PRODUCT → PLACE ON SCALE → CONFIRM WEIGHT → ADD TO   │
│ TICKET                                                      │
└─────────────────────────────────────────────────────────────┘

### Step-by-Step Process

**Step 1: Select a Scaled Product**
┌─────────────────┐
│ Product Screen  │
│ [Apple - $2/kg] │
│ [Banana - $1.5] │
│ [Meat - $10/kg] │ ← Select this
│ [Bread - $3/ea] │
└─────────────────┘
         ↓
┌─────────────────────────────────────────────────────────────┐
│              Product Scale Dialog Appears                  │
│                                                             │
│  Status: Scale connected                                    │
│  Weight: 0.000 kg                                          │
│  Price: $0.00                                              │
│  [Manual Input Field: 0.000]                               │
│  [Zero Scale] [Tare Scale]                                 │
│  [CANCEL] [OK]                                             │
└─────────────────────────────────────────────────────────────┘

**Step 2: Place Items on Scale**
- Place the product on your connected Aclas scale
- Watch the weight display update in real-time
- The weight automatically appears in the input field
- The price calculates automatically based on weight

**Step 3: Confirm the Weight**
- Verify the weight reading is accurate
- If satisfied with the weight, tap "OK"
- The product is added to your ticket with correct weight and price

---

## 5. Tare and Zero Functions

### Understanding Tare and Zero

┌─────────────────────────────────────────────────────────────┐
│                    TARE vs ZERO                             │
├─────────────────────────────────────────────────────────────┤
│ TARE: Remove container weight (e.g., basket, bag)           │
│ ZERO: Reset scale to 0.000 kg                               │
└─────────────────────────────────────────────────────────────┘

### Zero Function Usage

**When to Use Zero:**
- When starting with an empty scale
- To reset the scale to zero weight
- When you want to start fresh

**How to Use Zero:**
1. Ensure scale is empty
2. Tap "Zero Scale" button
3. Scale resets to 0.000 kg
4. Ready for new measurements

### Tare Function Usage

**When to Use Tare:**
- When using containers (baskets, bags, trays)
- To remove packaging weight
- To measure net product weight only

**How to Use Tare:**
1. Place container on scale
2. Wait for stable weight reading
3. Tap "Tare Scale" button
4. Container weight is saved
5. Only product weight will be measured

### Tare Example Scenario

┌─────────────────────────────────────────────────────────────┐
│                    TARE EXAMPLE                             │
├─────────────────────────────────────────────────────────────┤
│ Step 1: Place empty basket on scale → Shows 0.200 kg        │
│ Step 2: Tap "Tare Scale" → Scale shows 0.000 kg            │
│ Step 3: Add apples to basket → Shows 1.500 kg (net)        │
│ Step 4: Confirm weight → Only apple weight charged         │
└─────────────────────────────────────────────────────────────┘

---

## 6. Manual Weight Entry

### When Manual Entry is Needed

┌─────────────────────────────────────────────────────────────┐
│                MANUAL ENTRY SCENARIOS                       │
├─────────────────────────────────────────────────────────────┤
│ • No scale available                                      │
│ • Scale is temporarily out of order                         │
│ • Need to enter specific weight value                       │
│ • Testing without physical scale                            │
└─────────────────────────────────────────────────────────────┘

### Enabling Manual Entry

**Check Setting:**
1. Go to Settings → Scale Configuration
2. Verify "Enable Manual Weight Entry" is ON
3. When scale is not connected, manual entry is available

**Using Manual Entry:**
1. Select scaled product when scale is not connected
2. Weight input field becomes active
3. Type weight directly into the field
4. Price calculates automatically
5. Tap "OK" to confirm

---

## 7. Troubleshooting

### Common Issues and Solutions

┌─────────────────────────────────────────────────────────────┐
│                CONNECTION PROBLEMS                          │
├─────────────────────────────────────────────────────────────┤
│ Problem: Scale not found during scanning                    │
│ Solution: Check power, pairing mode, distance, Bluetooth    │
│                                                             │
│ Problem: Scale connected but no weight reading              │
│ Solution: Check scale placement, restart app, reconnect     │
│                                                             │
│ Problem: Weight not updating in real-time                   │
│ Solution: Check Bluetooth stability, scale battery          │
└─────────────────────────────────────────────────────────────┘

### Quick Fixes

**Connection Issues:**
1. Turn Bluetooth off and on again
2. Restart the OrtusPOS app
3. Move closer to the scale
4. Ensure scale is in pairing mode

**Weight Reading Issues:**
1. Check that nothing is blocking the scale
2. Verify scale is on a flat, stable surface
3. Clean the scale surface
4. Check scale battery level

---

## 8. Settings Configuration

### Scale Configuration Menu

┌─────────────────────────────────────────────────────────────┐
│                 SETTINGS MENU                                │
├─────────────────────────────────────────────────────────────┤
│ • Enable Manual Weight Entry [ON/OFF]                       │
│   └─ Allows manual weight entry when scale not connected   │
│ • Enable Virtual Scale [ON/OFF]                             │
│   └─ Allows testing with virtual scale (OFF in production) │
└─────────────────────────────────────────────────────────────┘

### Recommended Settings

**For Daily Operations:**
- Manual Weight Entry: ON (for flexibility)
- Virtual Scale: OFF (for security)

**For Testing:**
- Manual Weight Entry: ON (for testing flexibility)
- Virtual Scale: ON (to enable virtual scale testing)

---

## Best Practices

### For Accurate Weighing
✓ Use stable, flat surfaces for scales  
✓ Wait for weight to stabilize before confirming  
✓ Clean scales regularly  
✓ Calibrate scales according to manufacturer specifications  

### For Efficient Operations
✓ Pre-tare containers when possible  
✓ Train staff on proper tare/zero procedures  
✓ Keep scale area organized  
✓ Have manual entry procedure ready  

### For System Maintenance
✓ Regularly check Bluetooth connections  
✓ Update OrtusPOS app when available  
✓ Monitor scale battery levels  
✓ Protect scales from damage  

---

## Support Information

**For Technical Support:**
- Contact your OrtusPOS representative
- Verify scale functionality independently
- Check all connection settings
- Refer to this guide for common solutions

**System Requirements:**
- OrtusPOS version 2.24 or higher
- Compatible Aclas Bluetooth scale
- Android device with Bluetooth 4.0+
- Minimum Android 6.0 (API level 23)

---

*Document Version: 2.24*  
*Last Updated: January 2026*  
*OrtusPOS Scale Integration System*