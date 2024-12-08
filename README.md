# DESEncryption

**DESEncryption** is a pure Java implementation of the **Data Encryption Standard (DES)** algorithm, a symmetric-key block cipher used for encrypting and decrypting data. While DES is no longer considered secure for most applications, this implementation serves as a basic example of symmetric encryption and can be used for educational purposes.

---

## Features

- **Symmetric-Key Encryption**: Uses the same key for both encryption and decryption.
- **Block Cipher**: Operates on fixed-size 64-bit blocks.
- **Lightweight**: Pure Java implementation with no external dependencies.
- **Educational Tool**: Helps understand the fundamentals of block ciphers and encryption algorithms.

---

## Use Cases

- **Legacy Systems**: For systems that still rely on DES or need compatibility with older systems.
- **Educational Purposes**: Learn how symmetric block ciphers work.
- **Basic Encryption**: Encrypt small amounts of data where security is not a concern.

---

## Installation

Copy the `DESEncryption` class into your Java project. No additional libraries are required.

---

## Usage

### Basic Example
```java
public class Main {
    public static void main(String[] args) {
        String plaintext = "Hello, DES!";
        String key = "12345678"; 

        DESEncryption des = new DESEncryption(key);

        String ciphertext = des.encrypt(plaintext);
        System.out.println("Ciphertext: " + ciphertext);

        String decryptedText = des.decrypt(ciphertext);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}
