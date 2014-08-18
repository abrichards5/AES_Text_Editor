AES_Text_Editor
===============

Java GUI program to edit and encrypt text files using AES.

Capabilities
------------

  * Write notes and encrypt them with a password
  * Encrypt non-text (binary) files
  * Encrypt files via command line
  * Fast text search using [StringSearch](http://johannburkard.de/software/stringsearch/)
  
Details
-------
  * Raw text is handled in UTF-8 format
  * Text files when encrypted are stored encoded in base64
  * Encryption uses AES, CBC with PKCS5Padding, salted with a hard-coded byte array
  * Keys are hashed with PBKDF2WithHmacSHA1
  
  
