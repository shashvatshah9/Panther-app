# Panther-app
We ([lovlin-thakkar](https://github.com/lovlin-thakkar) & [shashvatshah9](https://github.com/shashvatshah9)) created this project about 3 years ago as a College Mini Project.
This is an Android App. (still needs changes, fixes, improvements)

We are using Steganography and encryption to secure chats using the Firebase Live Database.
The app is in its infancy and you may find many errors.

### Steganography
The practice of hiding messages / data in an image. 
This is done by tweaking pixel values of an image.
The strategy for tweaking the values can vary and the sender and reciever should communicate the strategy beforehand.

For instance, an example of a strategy is that, we'll convert the message to binary, convert the image into a binary string by flattening it; replace last 2 bits of every byte in the image binary string by 2 bits from the message. Then restructure the image and send it over. The receiver can reverse the process and generate the message back.

The exciting part is, this strategy doesn't change the image. The image looks almost exactly the same, as we are only changing each byte by a very small value (ranging from 0-3). 

Essentially we're storing the message as a noise in the image.

### Audio Steganography
We've done the same thing with audio. We converted .wav files to binary and hid the message data inside it.
We observed similar results like Image Steganography, no observable effects on the audio.
