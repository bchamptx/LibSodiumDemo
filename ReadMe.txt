LibSodium is a cryptographic library that is targeted for ease of use.
See https://www.gitbook.com/book/jedisct1/libsodium/details
    or
    https://download.libsodium.org/doc/

This is a demo of the Kalium Java bridge to the native libsodium library.
See https://github.com/abstractj/kalium

To build this demo:
	mvn clean install

This produces an executable jar and a lib directory containing dependent jars.

You must install libsodium native libraries on your target machine.
See https://download.libsodium.org/doc/installation/index.html

For OS X, you can use Homebrew like this:
	brew install libsodium

To run:
	cd to target directory containing jar and lib directory as created by maven build process.
	java -jar LibSodiumDemo.jar

Expected output. Note: Hex strings will be different each time you execute because new keys and nonce are generated each time you run.:

Alice's public key as listed on her web site: e7c43ffa1be155092a21d858123181b69a8f50c8830ed99e1c3ffc4b9ffec426
Bob's public key as listed on his web site: 767b3a20893973b926ee92931a1db6813239c43a342dd62b4d9c731241c15644

Alice's plain text message: Hi Bob. We attack at dawn. We attack at dawn. We attack at dawn. Cíaó!
Alice's nonce in hex: 55934c5149a5fcfab21e5f5da8a2bdfd3814bce1dff22165
Alice's encrypted message to Bob in hex: b99bc77d1200dd4a9d71d623a40379a3aa8b0d70eaf71de28d66b705e1a391e6a28eb56141fbfaa9f91670db92e57f3a4597fd4272521e844db4b8a81575d7111ff20c709f971a34890562dfb6b6d23bf8456d6b992eb2a3

Alice's message to Bob as decrypted by Bob: Hi Bob. We attack at dawn. We attack at dawn. We attack at dawn. Cíaó!
