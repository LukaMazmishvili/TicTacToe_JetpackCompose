package com.example.tictactoe_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeGame()
        }
    }
}

enum class Player { X, O, None }

@Composable
fun TicTacToeGame() {

    var cells by remember { mutableStateOf(Array(3) { Array(3) { Player.None } }) }
    var currentPlayer by remember { mutableStateOf(Player.X) }
    var gameResult by remember { mutableStateOf<GameResult?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (gameResult) {
                    GameResult.XWins -> "Player X wins!"
                    GameResult.OWins -> "Player O wins!"
                    GameResult.Draw -> "It's a draw!"
                    else -> "Current Player: ${currentPlayer.name}"
                },
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            GridBoard(cells, currentPlayer, gameResult) { row, col ->
                if (gameResult == null && cells[row][col] == Player.None) {
                    cells[row][col] = currentPlayer
                    currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
                    gameResult = checkGameResult(cells)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    cells = Array(3) { Array(3) { Player.None } }
                    currentPlayer = Player.X
                    gameResult = null
                },
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier
                    .height(48.dp)
                    .offset(0.dp, 20.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "Restart Game",
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun GridBoard(
    cells: Array<Array<Player>>,
    currentPlayer: Player,
    gameResult: GameResult?,
    onCellClick: (Int, Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(300.dp)
//            .border(1.dp, Color.Black, RectangleShape)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until 3) {
                Row {
                    for (col in 0 until 3) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                                .clickable {
                                    onCellClick(row, col)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (cells[row][col].name == "None") {
                                    ""
                                } else {
                                    cells[row][col].name
                                },
                                fontSize = 48.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class GameResult {
    XWins, OWins, Draw
}

fun checkGameResult(cells: Array<Array<Player>>): GameResult? {
    // Check rows, columns, and diagonals for a win
    for (i in 0 until 3) {
        if (cells[i][0] != Player.None &&
            cells[i][0] == cells[i][1] &&
            cells[i][1] == cells[i][2]
        ) {
            return if (cells[i][0] == Player.X) GameResult.XWins else GameResult.OWins
        }
        if (cells[0][i] != Player.None &&
            cells[0][i] == cells[1][i] &&
            cells[1][i] == cells[2][i]
        ) {
            return if (cells[0][i] == Player.X) GameResult.XWins else GameResult.OWins
        }
    }

    // Check diagonals
    if (cells[0][0] != Player.None &&
        cells[0][0] == cells[1][1] &&
        cells[1][1] == cells[2][2]
    ) {
        return if (cells[0][0] == Player.X) GameResult.XWins else GameResult.OWins
    }
    if (cells[0][2] != Player.None &&
        cells[0][2] == cells[1][1] &&
        cells[1][1] == cells[2][0]
    ) {
        return if (cells[0][2] == Player.X) GameResult.XWins else GameResult.OWins
    }

    // Check for a draw
    for (row in cells) {
        for (cell in row) {
            if (cell == Player.None) {
                return null
            }
        }
    }
    return GameResult.Draw
}

@Preview
@Composable
fun TicTacToeGamePreview() {
    TicTacToeGame()
}