#define _XOPEN_SOURCE 700

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#define ROWS 28
#define COLS 100

static char random_char(void) {
    static const char chars[] =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        "abcdefghijklmnopqrstuvwxyz"
        "0123456789"
        "<>/\\|{}[]()";
    return chars[rand() % (sizeof(chars) - 1)];
}

int main(void) {
    char screen[ROWS][COLS];
    int offset[COLS];
    int length[COLS];
    int speed[COLS];

    srand((unsigned int)time(NULL));

    for (int c = 0; c < COLS; c++) {
        offset[c] = rand() % ROWS;
        length[c] = 6 + rand() % 12;
        speed[c] = 1 + rand() % 3;
    }

    for (;;) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                screen[r][c] = ' ';
            }
        }

        for (int c = 0; c < COLS; c++) {
            for (int i = 0; i < length[c]; i++) {
                int r = (offset[c] + i) % ROWS;
                screen[r][c] = random_char();
            }

            offset[c] = (offset[c] + speed[c]) % ROWS;

            if (rand() % 30 == 0) {
                length[c] = 6 + rand() % 12;
                speed[c] = 1 + rand() % 3;
            }
        }

        printf("\033[H");
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (screen[r][c] != ' ') {
                    printf("\033[32m%c\033[0m", screen[r][c]);
                } else {
                    printf(" ");
                }
            }
            printf("\n");
        }

        fflush(stdout);

        struct timespec delay = {0, 50 * 1000 * 1000};
        nanosleep(&delay, NULL);
    }

    return 0;
}
