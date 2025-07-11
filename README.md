# Java Chess Game

This is a simple, graphical chess game implemented in Java. It allows two players to play a game of chess with standard rules, including piece movement, captures, pawn promotion, castling, and check/checkmate detection.

---

## Features

- **Standard Chess Rules**: Implements movement rules for all chess pieces (Pawn, Rook, Knight, Bishop, Queen, King).
- **Captures**: Pieces can capture opposing pieces according to chess rules.
- **Pawn Promotion**: Pawns let the user promote to a Queen, Knight, Rook, or Bishop upon reaching the last rank.
- **Castling**: Supports both Kingside and Queenside castling for both White and Black, with appropriate rule checks (e.g., king not in check, squares not attacked, no pieces in between).
- **Check Detection**: Identifies when a king is in check.
- **Checkmate & Stalemate Detection**: Determines the end of the game due to checkmate (one player wins) or stalemate (draw).
- **Turn Management**: Clearly indicates whose turn it is.

---

## Project Structure

* `Game.java`: The main entry point of the application. Manages the game loop, window creation, and rendering.
* `Display.java`: Handles all graphical rendering of the chessboard, pieces, possible moves, pawn promotion menu, and game-over screens.
* `MouseInput.java`: Processes mouse clicks for piece selection and movement. Contains the core game logic, including move validation, special moves (castling, pawn promotion), and check/checkmate/stalemate detection.
* `Window.java`: Responsible for creating the main JFrame for the game.
* `BufferedImageLoader.java`: A utility class for loading chess piece image assets.
* `SpriteSheet.java`: A utility class for extracting individual piece images from a spritesheet.

---

## Installation

To run this chess game, you will need a Java Development Kit (JDK) installed on your system.

1.  **Save the files**: Ensure `Game.java`, `Display.java`, `MouseInput.java`, `Window.java`, `BufferedImageLoader.java`, and `SpriteSheet.java` are in the same directory, preferably within a package structure like `com/your-name/main/`.
2.  **Compile**: Open a terminal or command prompt, navigate to the directory containing your `.java` files, and compile them.
    (Note: You might need to adjust the path based on your actual directory structure).
3.  **Run**: After successful compilation, run the `Game` class.

---
