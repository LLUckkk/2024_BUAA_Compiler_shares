.data
str_0: .asciiz " "




.text
jal main

j exit

main:

# %local_var_0 = alloca i32
addi $t0, $sp, -4


# store i32 0, i32* %local_var_0
li $k0 0
sw $k0, 0($t0)


# %local_var_1 = alloca i32
addi $t1, $sp, -8


# store i32 0, i32* %local_var_1
li $k0 0
sw $k0, 0($t1)


# %local_var_2 = load i32, i32* %local_var_1
lw $t2, 0($t1)


# %local_var_3 = add i32 %local_var_2, 1
li $k1 1
addu $t2, $t2, $k1


# store i32 %local_var_3, i32* %local_var_1
sw $t2, 0($t1)


# store i32 %local_var_3, i32* %local_var_0
sw $t2, 0($t0)


# %local_var_4 = load i32, i32* %local_var_0
lw $t0, 0($t0)


# %local_var_5 = load i32, i32* %local_var_1
lw $t1, 0($t1)


# call void @putint(i32 %local_var_4)

move $a0 $t0
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_0, i64 0, i64 0))
la $a0, str_0
li $v0 4
syscall

# call void @putint(i32 %local_var_5)

move $a0 $t1
li $v0 1
syscall

# ret i32 0
li $v0 0
jr $ra

exit:
li $v0 10
syscall
