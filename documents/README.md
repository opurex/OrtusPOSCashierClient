# Ortus POS Suite

> A powerful hybrid Point of Sale system for Android, Desktop, and Cloud SaaS environments  
> Maintained by **Opurex Ortus**

<div align="center">

[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20Desktop%20%7C%20Cloud-blue)](https://pos.opurex.com)
[![Java](https://img.shields.io/badge/Java-24-orange)](https://openjdk.org/projects/jdk/24/)
[![License](https://img.shields.io/badge/License-Custom%20Proprietary-red)](#licensing)
[![Maintained](https://img.shields.io/badge/Maintained%20by-Opurex%20Ortus-green)](https://www.opurex.com)

**[Website](http://www.opurex.com)** • **[SaaS Dashboard](https://pos.opurex.com)** • **[Private Repo](https://repo.opurex.com)** • **[GitHub Mirror](https://github.com/opurex)**

</div>

---

## Overview

**Ortus POS** is a multi-platform retail Point of Sale solution designed for cooperatives, supermarkets, restaurants, and multi-branch enterprises. Built with modern architecture and enterprise-grade reliability.

### Key Features

- **Real-time synchronization** across all devices and locations
- **Offline capability** - continue operations without internet
- **Multi-currency support** for international businesses
- **Advanced analytics** and reporting dashboard
- **Hardware agnostic** - works with any POS hardware setup
- **Scalable architecture** from single store to enterprise chains

### Target Markets

- **Cooperatives & Supermarkets** - Complete inventory management
- **Restaurants & Cafes** - Table management with kitchen integration
- **Multi-branch Enterprises** - Centralized control and reporting
- **Retail Chains** - Synchronized operations across locations

---

## Technology Stack

| **Platform** | **Technology** | **Purpose** |
|--------------|----------------|-------------|
| **Android POS** | Java 24, Gradle, Android SDK | Mobile terminals and tablets |
| **Desktop POS** | Java 24 (Cross-platform JRE) | Windows, macOS, Linux workstations |
| **Cloud Backend** | Python/Django, PostgreSQL | Centralized management and analytics |
| **Infrastructure** | Docker, Kubernetes, CI/CD | Scalable deployment and operations |

**Fully tested and optimized for Java 24**

---

## Getting Started

### Prerequisites

- Java 24+ installed
- Android SDK (for mobile development)
- Git access to repositories

### Android Development Setup

```bash
# Clone the repository
git clone https://repo.opurex.com/pos/opurex-android.git
cd opurex-android

# Configure Android SDK
export ANDROID_HOME=/path/to/android/sdk

# Install development build
./gradlew installVanillaDebug

# Run comprehensive tests
./gradlew test
```

### Desktop Application Setup

```bash
# Clone desktop version
git clone https://repo.opurex.com/pos/opurex-desktop.git
cd opurex-desktop

# Build application
./gradlew build

# Launch application
java -jar build/libs/ortus-pos-desktop.jar
```

---

## Core Workflows

### Weight-Based Product Sale

This workflow outlines how a product sold by weight (e.g., oranges) is handled from selection to ticket update.

1.  **Product Selection**: The process begins when a user selects a product from the `CatalogFragment`.

2.  **Scaled Product Check**: The `Transaction` activity receives the selection and checks if the product is a "scaled" item by calling `product.isScaled()`.

3.  **Weight Input Dialog**: If the product is scaled, the `askForAScaledProduct` method is invoked, which displays the `ProductScaleDialog`. This dialog is the primary interface for weight input.

4.  **Getting the Weight**: The dialog can receive weight in two ways:
    *   **Automatic (from Scale)**: The dialog's `ScaleManager` listens for data from the `BluetoothScaleHelper` (which uses the Aclas SDK). When weight is received, the `onWeightReceived` method updates the dialog's UI in real-time, showing the weight and the calculated price.
    *   **Manual Input**: The user can also type the weight directly into an `EditText` field. A `TextWatcher` ensures the price is recalculated and displayed as the user types.

5.  **Confirming the Weight**: The user presses "Ok" in the dialog. This action triggers the `onPsdPositiveClick` callback in the `Transaction` activity, passing the final product and weight information.

6.  **Updating the Ticket**: The `Transaction` activity then calls `addAScaledProductToTicket`, which delegates the task to the `TicketFragment`. The fragment, in turn, tells the current `Ticket` object to add the scaled product. A new `TicketLine` is created where the **weight is stored in the `quantity` field**.

7.  **UI Refresh**: Finally, the `TicketFragment`'s view is updated, and the new item appears on the ticket, showing the product name, the weight (as quantity), and the final calculated price.

---

## Build Configuration

### Available Build Variants

| **Build Type** | **Flavor** | **Target Use Case** | **Features Included** |
|----------------|------------|---------------------|----------------------|
| **Debug** | Vanilla | Development & Testing | Full debugging, extensive logging |
| **Release** | BNP | Business & Professional | Enterprise features, optimized performance |
| **Release** | WCR | Wholesale & Retail | Advanced inventory management |

### Build Commands

```bash
# Development builds
./gradlew assembleVanillaDebug
./gradlew installVanillaDebug

# Production builds
./gradlew assembleBnpRelease
./gradlew assembleWcrRelease

# Quality assurance
./gradlew check
./gradlew lint
```

---

## Hardware Compatibility

### Certified Android Terminals

- **TPOS Series** - Professional touch-screen terminals
- **J100 Series** - Compact countertop units
- **Sunmi Devices** - All-in-one integrated systems
- **Generic Android** - Any device running Android 8.0+

### Desktop Platform Support

- **Windows** 10/11 with Java 24 compatibility
- **macOS** 12+ supporting both Intel and Apple Silicon
- **Linux** distributions including Ubuntu 20.04+, CentOS 8+

### Peripheral Integration

- **Receipt Printers** - ESC/POS, Star, Epson protocols
- **Barcode Scanners** - USB and Bluetooth connectivity
- **Cash Drawers** - RJ11 and USB interfaces
- **Payment Terminals** - EMV chip readers, NFC/contactless

---

## Licensing Structure

### Important Licensing Notice

> **Note:** Ortus POS employs a hybrid licensing model combining proprietary and open-source components.

| **Component** | **License Type** | **Usage Rights** |
|---------------|------------------|------------------|
| **Core Business Logic** | Custom Proprietary (Opurex) | Commercial license required |
| **User Interface** | Custom Proprietary (Opurex) | Restricted distribution |
| **SaaS Platform** | Commercial Subscription | Service agreement required |
| **Open Source Libraries** | GPL/MIT/Apache | See `LICENSES.md` for details |

### Compliance Requirements

- GPL-licensed components maintain full source code availability
- Proprietary modules require commercial licensing for redistribution
- SaaS usage governed by Terms of Service agreement
- Complete third-party attributions documented in `LICENSES.md`

### Commercial Licensing

For enterprise deployment, white-label solutions, or commercial redistribution, contact our licensing team for appropriate commercial agreements.

---

## Deployment Options

### SaaS Platform (Recommended)

- **Instant deployment** at [pos.opurex.com](https://pos.opurex.com)
- **Automatic updates** and maintenance
- **Professional support** included
- **Scalable pricing** based on usage

### On-Premises Enterprise

- **Complete infrastructure control**
- **Custom ERP integration** via APIs
- **Enhanced security compliance**
- **Dedicated technical support**

### Containerized Deployment

```bash
# Deploy with Docker
docker pull opurex/ortus-pos:latest
docker run -p 8080:8080 -d opurex/ortus-pos:latest

# Deploy with Docker Compose
docker-compose up -d
```

---

## Community Engagement

### How to Contribute

We welcome community participation through:

- **Bug reporting** - Help improve software quality
- **Feature requests** - Share enhancement ideas
- **Beta testing** - Early access to new features
- **Documentation** - Improve user guides and tutorials

### Development Contributions

Due to our hybrid licensing model, direct code contributions have specific requirements. For partnership opportunities or contributor agreements, please contact our development team.

---

## Support Channels

### Contact Information

- **Website:** [www.opurex.com](http://www.opurex.com)
- **General Inquiries:** hello@opurex.com
- **Technical Support:** Available through SaaS dashboard
- **Commercial Licensing:** Dedicated licensing team

### Support Tiers

| **Support Level** | **Response Time** | **Availability** |
|-------------------|-------------------|------------------|
| **Community** | 48-72 hours | Business days |
| **Professional** | 24 hours | Business days |
| **Enterprise** | 4 hours | 24/7 coverage |

---

## Legal Disclaimer

This software incorporates and properly attributes third-party open-source components. While core business modules are proprietary and protected, we maintain full transparency and compliance with all applicable open-source licenses where required.

For complete licensing information, commercial terms, and legal compliance details, please refer to our official documentation or contact our legal team directly.