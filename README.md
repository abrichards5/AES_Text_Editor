AES_Text_Editor
===============

Java GUI program to edit and encrypt text files using AES.

Capabilities
------------

  * Write notes and encrypt them with a password
  * Encrypt non-text (binary) files
  * Encrypt files via command line
  * Fast text search using [StringSearch](http://johannburkard.de/software/stringsearch/)
  * Encode to Base64, Hex or raw (no encoding/binary)
  
Details
-------
  * Raw text is handled in UTF-8 format
  * Text files when encrypted are by default encoded in base64
  * Binary files when encrypted by default have no encoding

### Encryption
  * **Cipher**: `AES-128`
  * **Block Mode**: `CBC`
  * **Padding**: `PKCS5Padding`
  * **HMAC**: `SHA256`
  * **Key Derivation**: `PBKDF2WithHmacSHA1`
  * **Salting**: `SHA1PRNG`

  
Sources
-------

  * [App icon](http://www.iconarchive.com/show/glaze-icons-by-mart/encrypted-icon.html)
  * [StringSearch](http://johannburkard.de/software/stringsearch/)
  * [Apache Commons CLI](http://commons.apache.org/proper/commons-cli/index.html)
  * [FontChooser](http://jfontchooser.sourceforge.jp/site/jfontchooser/project-summary.html) : Slightly modified from source (Added a default button)
  * [RequestFocusListener](http://tips4java.wordpress.com/2010/03/14/dialog-focus/)
    
  
