const int a_1 = 1;
const int a_2 = 2, a_3 = 1 + (2 * 3);
const char b_1 = 'a';
const char b_2 = 'b', b_3 = 'c', b_4 = 'd';

int c_1 = 1;
int c_2 = 2, c_3 = 3, c_4 = 4;
char d_1 = 'a';
char d_2 = 'b', d_3 = 'c';

void func_1(int a, char b) {
	/* declare */
	int c;
	/* statement */
	c = 1;
	printf("%d %c\n",a - c, b);
}

int func_2(int a, int b, int c) {
	return (a * b) + c; //return with exp
}

char func_3(char a) {
	return a;
}

void func_4() {
	printf("22373321\n");
	return; //return no exp
}

int func_5() {
	int a = 1;
	return a;
}

char func_6() {
	char a = 'b';
	return a;
}


int main() {
	/* correct */
	int x = 1;
	char ch = 'z';
	/* statement */
	x = 2; //lval
	{
		char chh = 'x';
	}
	/* if */
	int n;
	if(a_1 == 1) {
		n = 0;
	}
	if(a_2 != 2) {
		n = 0;
	} else {
		n = 0;
	}
	/* for */
	int i = 0;
	int sum = 0;
	for(i = 0; i <= 1; i = i + 1) {
		sum = sum + i;
	}
	i = 0;
	for( ; i < 3; i = i + 1) {
		sum = sum + i;
	}
	for(i = 9; ; i = i - 1) {
		if(i > 5) {
			break;
		}
	}
	for(i = 0; i < 10; ) {
		i = i + 1;
	}
	for(i = 0; ; ) {
		i = i + 1;
		if(i == 2) {
			continue;
		}
		if(i == 3) {
			break;
		}
	}
	i = 1;
	for( ; i <= 3; ) {
		i = i + 1;
	}
	i = 0;
	for( ; ; i = i + 1) {
		if(i == 4) {
			break;
		}
	}
	i = 0;
	for( ; ; ) {
		i = i + 1;
		if(i == 1) {
			break;
		}
	}
	/* getint/char */
	int get_x;
	char get_y;
	get_x = getint();
	get_y = getchar();
	/* multExp */
	int mult_1 = func_2(1, 2, 3) + (3 + 2);
	int mult_2 = a_1 * a_2;
	int mult_3 = a_2 / a_1;
	int mult_4 = a_2 % a_1;
	/* AddExp */
	int add_1 = a_2 * a_3 + a_2 % a_1;
	int add_2 = a_2 * a_3 - a_2 % a_1;
	
	/* correct */
	printf("22373321\n");
	int a = 0;
	return 0;
}

