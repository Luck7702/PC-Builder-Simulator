# PC Builder Simulator

A Java + JavaFX desktop application that simulates building custom PCs.  
Users can buy parts, manage inventory, assemble builds, validate hardware compatibility, and run performance benchmarks.

---

## Overview

This project simulates real-world PC building logic.  
It models hardware relationships and enforces compatibility rules between components.

Core features:
- CSV-based component catalog
- Inventory management
- Multiple PC builds
- Hardware compatibility checks
- Benchmark simulation
- Save / Load system

---

## Features

### Component Shop
- Browse parts by category:
  - CPU
  - GPU
  - RAM
  - Storage
  - Motherboard
  - Power Supply
- Purchase parts using a virtual budget
- Catalog loaded dynamically from CSV

### Inventory System
- Purchased parts stored in inventory
- Install components into builds
- Remove parts back to inventory
- Inventory persists between sessions

### PC Build System
- Create multiple builds
- Switch active build
- Install and remove components
- Clear builds and return parts

### Compatibility Validation
The system enforces real hardware rules:

- CPU socket must match motherboard
- RAM type (DDR4 / DDR5) must match motherboard
- PSU wattage must be sufficient
- Component limits:
  - Max 1 CPU
  - Max 1 GPU
  - Max 1 PSU
  - Max 1 Motherboard

### Benchmark System
Benchmark runs only if build is complete.

Required components:
- CPU
- GPU
- RAM
- Storage
- Motherboard
- Power Supply

Validation:
- PSU wattage must exceed total system draw

Benchmark stages:
- CPU
- GPU
- Memory
- Storage

Outputs:
- Individual scores
- Total score
- System tier classification

### Save and Load
- Inventory saved to disk
- Builds saved to disk
- Budget saved
- Auto-loaded on startup

---

## Project Structure

PCBuilderApp/
├── app/
│   ├── Main.java
│   ├── AppController.java
│   ├── BenchmarkView.java
│   ├── ShopView.java
│   ├── InventoryView.java
│   ├── WorkbenchView.java
│
├── classes/
│   ├── Component.java
│   ├── CPU.java
│   ├── GPU.java
│   ├── RAM.java
│   ├── Storage.java
│   ├── Motherboard.java
│   ├── PowerSupply.java
│   ├── PCBuild.java
│   ├── Inventory.java
│   ├── FileHandler.java
│   ├── BenchmarkService.java
│
├── data/
│   ├── component_catalog.csv
│   ├── inventory_data.txt
│   ├── builds_data.txt
│
├── run.sh
└── README.md

---

## CSV Format

File: data/component_catalog.csv

Type,Brand,Name,Price,Score,Spec1,Spec2,Spec3,Power

Examples:

CPU,Intel,Core i5-13400,230,650,10,2.5,LGA1700,65 
GPU,NVIDIA,RTX 3060,330,950,12,1320,1750,170 
RAM,Corsair,Vengeance 16GB,90,300,16,3600,DDR4,6 
STORAGE,Samsung,980 1TB NVMe,110,450,1000,NVMe,SSD,5 
PSU,Corsair,CX650M,75,90,650,Bronze,Semi-Modular,0 
MOBO,ASUS,TUF B660,170,460,4,DDR4,LGA1700,12 

---

## Component Field Mapping

CPU
- cores
- clock speed
- socket

GPU
- VRAM
- core clock
- memory clock

RAM
- capacity
- speed
- DDR type

Storage
- capacity
- interface
- type

PSU
- wattage
- rating

Motherboard
- socket
- RAM type
- max slots

---

## Requirements

- Java 17+
- JavaFX
- Linux / macOS / Windows


---

## Benchmark Rules

Benchmark will NOT start if:
- Missing CPU
- Missing GPU
- Missing RAM
- Missing Storage
- Missing Motherboard
- Missing PSU
- PSU wattage insufficient

---

## Validation Rules

- CPU socket must match motherboard
- RAM type must match motherboard
- Only one of:
  - CPU
  - GPU
  - PSU
  - Motherboard
- PSU must supply enough power

---


## Object-Oriented Programming (OOP)

This project is built using Object-Oriented Programming principles. 
Each hardware part and system feature is modeled as an object with real-world behavior.

---

### 1. Encapsulation

Encapsulation means hiding internal data and exposing only what is needed.

Each component class (CPU, GPU, RAM, etc.) has:
- private variables
- public getter methods

Example:

protected int score;

public int getScore() {
    return score;
}

This prevents direct modification of internal values and keeps data safe.

---

### 2. Inheritance

All hardware components inherit from a base class:

Component 
├── CPU 
├── GPU 
├── RAM
├── Storage 
├── Motherboard 
└── PowerSupply 

The Component class contains shared attributes:
- brand 
- name 
- price 
- power 
- score 

Each child class adds its own properties:
- CPU → cores, speed, socket 
- GPU → VRAM, clocks 
- RAM → type, speed 
- PSU → wattage, rating 

This avoids code duplication and keeps the structure clean.

---

### 3. Polymorphism

Polymorphism allows different objects to be treated as the same type.

All parts are stored as Component:

List<Component> installedComponents;

But at runtime:
- CPU behaves as CPU 
- GPU behaves as GPU 
- RAM behaves as RAM

We can check their real type:

if (c instanceof CPU) { ... }

This allows flexible logic while keeping generic storage.

---

### 4. Abstraction

The Component class acts as an abstraction layer.

It defines:
- getPrice() 
- getPower() 
- getScore() 
- getType() 

Each child class provides real behavior.

This lets the system interact with parts 
without caring about the exact class.

---

### 5. Composition

A PC build contains components.

class PCBuild {
    private List<Component> installedComponents;
}

This represents a real-world relationship:
- A PC is made of parts 
- Parts exist independently 

---

### 6. Separation of Responsibility

Each class has a clear role:

Component → Base hardware model 
CPU / GPU / RAM → Hardware behavior 
Inventory → Stores owned parts 
PCBuild → Represents a PC 
FileHandler → Saving / loading 
AppController → Business logic 
Views → UI display 

This improves maintainability and readability.

---

### Why OOP fits this project

- Hardware naturally maps to objects 
- Real-world relationships are preserved 
- Easy to extend:
  - add new parts 
  - add new rules 
- Clean code structure 
- Easier debugging 

---

### Conclusion

OOP allows this simulator to:

- behave like real hardware systems 
- enforce compatibility rules 
- remain scalable 
- remain readable 

Without OOP, this project would be much harder to manage and extend.








## Author

Dennis Christian Hansell 2802432302

---

## License

Educational / personal projec









