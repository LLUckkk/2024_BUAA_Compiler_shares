#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<ctype.h>
#include<string.h>
#include<time.h>
#define ll long long
#define eps 1e-9
void print_str(char str[]) {
	int i = 0;
	for (; str[i] != 0; i = i + 1) {
		printf("%c", str[i]);
	}
	printf("\n");
}

int main() {
	char src[32] = "a";
	char dst[32] = "a";
	print_str(dst);
	int i = 0;
	for (; src[i] != 0; i = i + 1) {
		dst[i] = src[i];
	}
	dst[i] = 0;
	print_str(dst);

	return 0;
}
