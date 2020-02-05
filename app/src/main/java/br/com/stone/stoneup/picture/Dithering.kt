package br.com.stone.stoneup.picture

import android.graphics.Bitmap
import android.graphics.Color

/**
 * @author filpgame
 * @since 2017-08-08
 */
object Dithering {
    var threshold: Int = 128
}


/**
 * 2 by 2 Bayer Ordered Dithering
 *
 * 1 3
 * 4 2
 *
 * (1/5)
 *
 */
fun Bitmap.ordered2By2Bayer(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val matrix = arrayOf(intArrayOf(1, 3), intArrayOf(4, 2))

    val width = width
    val height = height
    for (y in 0 until height) {
        for (x in 0 until width) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red

            gray += gray * matrix[x % 2][y % 2] / 5

            if (gray < Dithering.threshold) {
                gray = 0
            } else {
                gray = 255
            }

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * 3 by 3 Bayer Ordered Dithering
 *
 *  3 7 4
 *  6 1 9
 *  2 8 5
 *
 *  (1/10)
 */
fun Bitmap.ordered3By3Bayer(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val matrix = arrayOf(intArrayOf(3, 7, 4), intArrayOf(6, 1, 9), intArrayOf(2, 8, 5))

    val width = width
    val height = height
    for (y in 0 until height) {
        for (x in 0 until width) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red

            gray += gray * matrix[x % 3][y % 3] / 10

            if (gray < Dithering.threshold) {
                gray = 0
            } else {
                gray = 255
            }

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}


/**
 * 4 by 4 Bayer Ordered Dithering
 *
 *  1 9 3 11
 *  13 5 15 7
 *  4 12 2 10
 *  16 8 14 6
 *  (1/17)
 */
fun Bitmap.ordered4By4Bayer(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val matrix = arrayOf(intArrayOf(1, 9, 3, 11), intArrayOf(13, 5, 15, 7), intArrayOf(4, 12, 2, 10), intArrayOf(16, 8, 14, 6))

    val width = width
    val height = height
    for (y in 0 until height) {
        for (x in 0 until width) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red

            gray += gray * matrix[x % 4][y % 4] / 17

            if (gray < Dithering.threshold) {
                gray = 0
            } else {
                gray = 255
            }

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}


/**
 * 8 by 8 Bayer Ordered Dithering
 *
 *  1 49 13 61 4 52 16 64
 *  33 17 45 29 36 20 48 32
 *  9 57 5 53 12 60 8 56
 *  41 25 37 21 44 28 40 24
 *  3 51 15 63 2 50 14 62
 *  35 19 47 31 34 18 46 30
 *  11 59 7 55 10 58 6 54
 *  43 27 39 23 42 26 38 22
 *
 *  (1/65)
 */
fun Bitmap.ordered8By8Bayer(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val matrix = arrayOf(intArrayOf(1, 49, 13, 61, 4, 52, 16, 64), intArrayOf(33, 17, 45, 29, 36, 20, 48, 32), intArrayOf(9, 57, 5, 53, 12, 60, 8, 56), intArrayOf(41, 25, 37, 21, 44, 28, 40, 24), intArrayOf(3, 51, 15, 63, 2, 50, 14, 62), intArrayOf(35, 19, 47, 31, 34, 18, 46, 30), intArrayOf(11, 59, 7, 55, 10, 58, 6, 54), intArrayOf(43, 27, 39, 23, 42, 26, 38, 22))

    val width = width
    val height = height
    for (y in 0 until height) {
        for (x in 0 until width) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red

            gray += gray * matrix[x % 8][y % 8] / 65

            if (gray < Dithering.threshold) {
                gray = 0
            } else {
                gray = 255
            }

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}


/**
 * Floyd-Steinberg Dithering
 *
 *   X 7
 * 3 5 1
 *
 * (1/16)
 */
fun Bitmap.floydSteinberg(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 1) {
        for (x in 1 until width - 1) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }
            errors[x + 1][y] += 7 * error / 16
            errors[x - 1][y + 1] += 3 * error / 16
            errors[x][y + 1] += 5 * error / 16
            errors[x + 1][y + 1] += 1 * error / 16

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * Jarvis, Judice , Ninke Dithering
 *
 *     X 7 5
 * 3 5 7 5 3
 * 1 3 5 3 1
 *
 * (1/48)
 */
fun Bitmap.jarvisJudiceNinke(): Bitmap {
    val out = Bitmap.createBitmap(width, height,
            config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 2) {
        for (x in 2 until width - 2) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }

            errors[x + 1][y] += 7 * error / 48
            errors[x + 2][y] += 5 * error / 48

            errors[x - 2][y + 1] += 3 * error / 48
            errors[x - 1][y + 1] += 5 * error / 48
            errors[x][y + 1] += 7 * error / 48
            errors[x + 1][y + 1] += 5 * error / 48
            errors[x + 2][y + 1] += 3 * error / 48

            errors[x - 2][y + 2] += 1 * error / 48
            errors[x - 1][y + 2] += 3 * error / 48
            errors[x][y + 2] += 5 * error / 48
            errors[x + 1][y + 2] += 3 * error / 48
            errors[x + 2][y + 2] += 1 * error / 48

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * Sierra Dithering
 *
 *     X 5 3
 * 2 4 5 4 2
 *   2 3 2
 *
 * (1/32)
 */
fun Bitmap.sierra(): Bitmap {
    val out = Bitmap.createBitmap(width, height,
            config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 2) {
        for (x in 2 until width - 2) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }

            errors[x + 1][y] += 5 * error / 32
            errors[x + 2][y] += 3 * error / 32

            errors[x - 2][y + 1] += 2 * error / 32
            errors[x - 1][y + 1] += 4 * error / 32
            errors[x][y + 1] += 5 * error / 32
            errors[x + 1][y + 1] += 4 * error / 32
            errors[x + 2][y + 1] += 2 * error / 32

            errors[x - 1][y + 2] += 2 * error / 32
            errors[x][y + 2] += 3 * error / 32
            errors[x + 1][y + 2] += 2 * error / 32

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * Two-Row Sierra Dithering
 *
 *     X 4 3
 * 1 2 3 2 1
 *
 * (1/16)
 */
fun Bitmap.twoRowSierra(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 1) {
        for (x in 2 until width - 2) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }

            errors[x + 1][y] += 4 * error / 16
            errors[x + 2][y] += 3 * error / 16

            errors[x - 2][y + 1] += 1 * error / 16
            errors[x - 1][y + 1] += 2 * error / 16
            errors[x][y + 1] += 3 * error / 16
            errors[x + 1][y + 1] += 2 * error / 16
            errors[x + 2][y + 1] += 1 * error / 16

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * Sierra Lite Dithering
 *
 *   X 2
 * 1 1
 *
 * (1/4)
 */
fun Bitmap.sierraLite(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 1) {
        for (x in 1 until width - 1) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }

            errors[x + 1][y] += 2 * error / 4

            errors[x - 1][y + 1] += 1 * error / 4
            errors[x][y + 1] += 1 * error / 4

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * Atkinson Dithering
 *
 *   X 1 1
 * 1 1 1
 *   1
 *
 * (1/8)
 */
fun Bitmap.atkinson(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 2) {
        for (x in 1 until width - 2) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }

            errors[x + 1][y] += error / 8
            errors[x + 2][y] += error / 8

            errors[x - 1][y + 1] += error / 8
            errors[x][y + 1] += error / 8
            errors[x + 1][y + 1] += error / 8

            errors[x][y + 2] += error / 8

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * Stucki Dithering
 *
 *     X 8 4
 * 2 4 8 4 2
 * 1 2 4 2 1
 *
 * (1/42)
 */
fun Bitmap.stucki(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 2) {
        for (x in 2 until width - 2) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }

            errors[x + 1][y] += 8 * error / 42
            errors[x + 2][y] += 4 * error / 42

            errors[x - 2][y + 1] += 2 * error / 42
            errors[x - 1][y + 1] += 4 * error / 42
            errors[x][y + 1] += 8 * error / 42
            errors[x + 1][y + 1] += 4 * error / 42
            errors[x + 2][y + 1] += 2 * error / 42

            errors[x - 2][y + 2] += 1 * error / 42
            errors[x - 1][y + 2] += 2 * error / 42
            errors[x][y + 2] += 4 * error / 42
            errors[x + 1][y + 2] += 2 * error / 42
            errors[x + 2][y + 2] += 1 * error / 42

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * Burkes Dithering
 *
 *     X 8 4
 * 2 4 8 4 2
 *
 * (1/32)
 */
fun Bitmap.burkes(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 1) {
        for (x in 2 until width - 2) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }

            errors[x + 1][y] += 8 * error / 32
            errors[x + 2][y] += 4 * error / 32

            errors[x - 2][y + 1] += 2 * error / 32
            errors[x - 1][y + 1] += 4 * error / 32
            errors[x][y + 1] += 8 * error / 32
            errors[x + 1][y + 1] += 4 * error / 32
            errors[x + 2][y + 1] += 2 * error / 32

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

/**
 * False Floyd-Steinberg Dithering
 *
 * X 3
 * 3 2
 *
 * (1/8)
 */
fun Bitmap.falseFloydSteinberg(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    var error: Int
    val errors = Array(width) { IntArray(height) }
    for (y in 0 until height - 1) {
        for (x in 1 until width - 1) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            if (gray + errors[x][y] < Dithering.threshold) {
                error = gray + errors[x][y]
                gray = 0
            } else {
                error = gray + errors[x][y] - 255
                gray = 255
            }
            errors[x + 1][y] += 3 * error / 8
            errors[x][y + 1] += 3 * error / 8
            errors[x + 1][y + 1] += 2 * error / 8

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

fun Bitmap.simpleLeftToRightErrorDiffusion(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    for (y in 0 until height) {
        var error = 0

        for (x in 0 until width) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red
            var delta: Int

            if (gray + error < Dithering.threshold) {
                delta = gray
                gray = 0
            } else {
                delta = gray - 255
                gray = 255
            }

            if (Math.abs(delta) < 10)
                delta = 0

            error += delta

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

fun Bitmap.randomDithering(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height
    for (y in 0 until height) {

        for (x in 0 until width) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red

            val threshold = (Math.random() * 1000).toInt() % 256

            if (gray < Dithering.threshold) {
                gray = 0
            } else {
                gray = 255
            }

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}

fun Bitmap.simpleThreshold(): Bitmap {
    val out = Bitmap.createBitmap(width, height, config)

    var alpha: Int
    var red: Int
    var pixel: Int
    var gray: Int

    val width = width
    val height = height

    for (y in 0 until height) {
        for (x in 0 until width) {

            pixel = getPixel(x, y)

            alpha = Color.alpha(pixel)
            red = Color.red(pixel)

            gray = red

            if (gray < Dithering.threshold) {
                gray = 0
            } else {
                gray = 255
            }

            out.setPixel(x, y, Color.argb(alpha, gray, gray, gray))
        }
    }

    return out
}