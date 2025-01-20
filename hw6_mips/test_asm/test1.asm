.data
global_var_0: .word 10, 20, 30

global_var_1: .byte 65, 66, 67, 68, 69

global_var_2: .byte 97, 98, 99, 0, 0

global_var_3: .word 0:5

global_var_4: .byte 0:5

str_0: .asciiz "Function with parameters: a = "

str_1: .asciiz ", b = "

str_2: .asciiz " arr[0] = "

str_3: .asciiz ", str[0] = "

str_4: .asciiz "\n"

str_5: .asciiz "Sum in func_with_param: "

str_6: .asciiz "\n"

str_7: .asciiz "22373141\n"

str_8: .asciiz "Negative intArray[0]: "

str_9: .asciiz "\n"

str_10: .asciiz "Positive intArray[0]: "

str_11: .asciiz "\n"

str_12: .asciiz "Quotient: "

str_13: .asciiz ", Remainder: "

str_14: .asciiz "\n"

str_15: .asciiz "Sum of ASCII codes1: "

str_16: .asciiz " "

str_17: .asciiz "\n"

str_18: .asciiz "Sum of ASCII codes2: "

str_19: .asciiz " "

str_20: .asciiz "\n"

str_21: .asciiz "x1 = "

str_22: .asciiz "\n"

str_23: .asciiz "a1 = "

str_24: .asciiz ", as char: "

str_25: .asciiz "\n"




.text
jal main

j exit

func_func_with_param:
block_0:

# %local_var_0 = alloca i32
addi $t0, $sp, -20


# store i32 %param_0, i32* %local_var_0
sw $a1, 0($t0)


# %local_var_1 = alloca i8
addi $t1, $sp, -21


# store i8 %param_1, i8* %local_var_1
sb $a2, 0($t1)


# %local_var_2 = load i32, i32* %local_var_0
lw $t2, 0($t0)


# %local_var_3 = load i8, i8* %local_var_1
lb $t3, 0($t1)


# %local_var_4 = getelementptr inbounds i32, i32* %param_2, i32 0
addi $t4, $a3, 0


# %local_var_5 = load i32, i32* %local_var_4
lw $t4, 0($t4)


# %local_var_6 = getelementptr inbounds i8, i8* %param_3, i32 0
lw $k0, -16($sp)

addi $t5, $k0, 0


# %local_var_7 = load i8, i8* %local_var_6
lb $t5, 0($t5)


# call void @putstr(i8* getelementptr inbounds ([31 x i8], [31 x i8]* @str_0, i64 0, i64 0))
la $a0, str_0
li $v0 4
syscall

# call void @putint(i32 %local_var_2)

move $a0 $t2
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([7 x i8], [7 x i8]* @str_1, i64 0, i64 0))
la $a0, str_1
li $v0 4
syscall

# %local_var_11 = zext i8 %local_var_3 to i32
sb $t3, -28($sp)


# call void @putch(i32 %local_var_11)

lw $a0, -28($sp)

li $v0 11
syscall

# call void @putstr(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @str_2, i64 0, i64 0))
la $a0, str_2
li $v0 4
syscall

# call void @putint(i32 %local_var_5)

move $a0 $t4
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([12 x i8], [12 x i8]* @str_3, i64 0, i64 0))
la $a0, str_3
li $v0 4
syscall

# %local_var_16 = zext i8 %local_var_7 to i32
sb $t5, -32($sp)


# call void @putch(i32 %local_var_16)

lw $a0, -32($sp)

li $v0 11
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_4, i64 0, i64 0))
la $a0, str_4
li $v0 4
syscall

# %local_var_19 = alloca i32
addi $s3, $sp, -36


# %local_var_20 = load i32, i32* %local_var_0
lw $t0, 0($t0)


# %local_var_21 = load i8, i8* %local_var_1
lb $t1, 0($t1)


# %local_var_22 = zext i8 %local_var_21 to i32
sb $t1, -40($sp)


# %local_var_23 = add i32 %local_var_20, %local_var_22
lw $k1, -40($sp)

addu $t0, $t0, $k1


# %local_var_24 = getelementptr inbounds i32, i32* %param_2, i32 0
addi $t1, $a3, 0


# %local_var_25 = load i32, i32* %local_var_24
lw $t1, 0($t1)


# %local_var_26 = add i32 %local_var_23, %local_var_25
addu $t0, $t0, $t1


# %local_var_27 = getelementptr inbounds i8, i8* %param_3, i32 0
lw $k0, -16($sp)

addi $t1, $k0, 0


# %local_var_28 = load i8, i8* %local_var_27
lb $t1, 0($t1)


# %local_var_29 = zext i8 %local_var_28 to i32
sb $t1, -44($sp)


# %local_var_30 = add i32 %local_var_26, %local_var_29
lw $k1, -44($sp)

addu $t0, $t0, $k1


# store i32 %local_var_30, i32* %local_var_19
sw $t0, 0($s3)


# %local_var_31 = load i32, i32* %local_var_19
lw $t0, 0($s3)


# call void @putstr(i8* getelementptr inbounds ([25 x i8], [25 x i8]* @str_5, i64 0, i64 0))
la $a0, str_5
li $v0 4
syscall

# call void @putint(i32 %local_var_31)

move $a0 $t0
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_6, i64 0, i64 0))
la $a0, str_6
li $v0 4
syscall

# %local_var_35 = load i32, i32* %local_var_19
lw $s3, 0($s3)


# ret i32 %local_var_35
move $v0 $s3
jr $ra

main:

# call void @putstr(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @str_7, i64 0, i64 0))
la $a0, str_7
li $v0 4
syscall

# %local_var_1 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t1, $k0, 0


# %local_var_2 = getelementptr inbounds [3 x i32], [3 x i32]* @global_var_0, i32 0, i32 0
la $k0, global_var_0
addi $t2, $k0, 0


# %local_var_3 = load i32, i32* %local_var_2
lw $t2, 0($t2)


# store i32 %local_var_3, i32* %local_var_1
sw $t2, 0($t1)


# %local_var_4 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 1
la $k0, global_var_3
addi $t1, $k0, 4


# %local_var_5 = getelementptr inbounds [3 x i32], [3 x i32]* @global_var_0, i32 0, i32 1
la $k0, global_var_0
addi $t2, $k0, 4


# %local_var_6 = load i32, i32* %local_var_5
lw $t2, 0($t2)


# store i32 %local_var_6, i32* %local_var_4
sw $t2, 0($t1)


# %local_var_7 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 2
la $k0, global_var_3
addi $t1, $k0, 8


# %local_var_8 = getelementptr inbounds [3 x i32], [3 x i32]* @global_var_0, i32 0, i32 2
la $k0, global_var_0
addi $t2, $k0, 8


# %local_var_9 = load i32, i32* %local_var_8
lw $t2, 0($t2)


# store i32 %local_var_9, i32* %local_var_7
sw $t2, 0($t1)


# %local_var_10 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 3
la $k0, global_var_3
addi $t1, $k0, 12


# %local_var_11 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t2, $k0, 0


# %local_var_12 = load i32, i32* %local_var_11
lw $t2, 0($t2)


# %local_var_13 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 1
la $k0, global_var_3
addi $t3, $k0, 4


# %local_var_14 = load i32, i32* %local_var_13
lw $t3, 0($t3)


# %local_var_15 = add i32 %local_var_12, %local_var_14
addu $t2, $t2, $t3


# store i32 %local_var_15, i32* %local_var_10
sw $t2, 0($t1)


# %local_var_16 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 4
la $k0, global_var_3
addi $t1, $k0, 16


# %local_var_17 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 3
la $k0, global_var_3
addi $t2, $k0, 12


# %local_var_18 = load i32, i32* %local_var_17
lw $t2, 0($t2)


# %local_var_19 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 2
la $k0, global_var_3
addi $t3, $k0, 8


# %local_var_20 = load i32, i32* %local_var_19
lw $t3, 0($t3)


# %local_var_21 = add i32 %local_var_18, %local_var_20
addu $t2, $t2, $t3


# store i32 %local_var_21, i32* %local_var_16
sw $t2, 0($t1)


# %local_var_22 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t1, $k0, 0


# %local_var_23 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t2, $k0, 0


# %local_var_24 = load i32, i32* %local_var_23
lw $t2, 0($t2)


# %local_var_25 = sub i32 0, %local_var_24
li $k0 0
subu $t2, $k0, $t2


# store i32 %local_var_25, i32* %local_var_22
sw $t2, 0($t1)


# %local_var_26 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t1, $k0, 0


# %local_var_27 = load i32, i32* %local_var_26
lw $t1, 0($t1)


# call void @putstr(i8* getelementptr inbounds ([23 x i8], [23 x i8]* @str_8, i64 0, i64 0))
la $a0, str_8
li $v0 4
syscall

# call void @putint(i32 %local_var_27)

move $a0 $t1
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_9, i64 0, i64 0))
la $a0, str_9
li $v0 4
syscall

# %local_var_31 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t4, $k0, 0


# %local_var_32 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t5, $k0, 0


# %local_var_33 = load i32, i32* %local_var_32
lw $t5, 0($t5)


# store i32 %local_var_33, i32* %local_var_31
sw $t5, 0($t4)


# %local_var_34 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t4, $k0, 0


# %local_var_35 = load i32, i32* %local_var_34
lw $t4, 0($t4)


# call void @putstr(i8* getelementptr inbounds ([23 x i8], [23 x i8]* @str_10, i64 0, i64 0))
la $a0, str_10
li $v0 4
syscall

# call void @putint(i32 %local_var_35)

move $a0 $t4
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_11, i64 0, i64 0))
la $a0, str_11
li $v0 4
syscall

# %local_var_39 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 1
la $k0, global_var_3
addi $t7, $k0, 4


# %local_var_40 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 3
la $k0, global_var_3
addi $s0, $k0, 12


# %local_var_41 = load i32, i32* %local_var_40
lw $s0, 0($s0)


# %local_var_42 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 2
la $k0, global_var_3
addi $s1, $k0, 8


# %local_var_43 = load i32, i32* %local_var_42
lw $s1, 0($s1)


# %local_var_44 = sdiv i32 %local_var_41, %local_var_43
div $s0 $s1
mflo $s0

# store i32 %local_var_44, i32* %local_var_39
sw $s0, 0($t7)


# %local_var_45 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 2
la $k0, global_var_3
addi $t7, $k0, 8


# %local_var_46 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 3
la $k0, global_var_3
addi $s0, $k0, 12


# %local_var_47 = load i32, i32* %local_var_46
lw $s0, 0($s0)


# %local_var_48 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 2
la $k0, global_var_3
addi $s1, $k0, 8


# %local_var_49 = load i32, i32* %local_var_48
lw $s1, 0($s1)


# %local_var_50 = srem i32 %local_var_47, %local_var_49
div $s0 $s1
mfhi $s0

# store i32 %local_var_50, i32* %local_var_45
sw $s0, 0($t7)


# %local_var_51 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 1
la $k0, global_var_3
addi $t7, $k0, 4


# %local_var_52 = load i32, i32* %local_var_51
lw $t7, 0($t7)


# %local_var_53 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 2
la $k0, global_var_3
addi $s0, $k0, 8


# %local_var_54 = load i32, i32* %local_var_53
lw $s0, 0($s0)


# call void @putstr(i8* getelementptr inbounds ([11 x i8], [11 x i8]* @str_12, i64 0, i64 0))
la $a0, str_12
li $v0 4
syscall

# call void @putint(i32 %local_var_52)

move $a0 $t7
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([14 x i8], [14 x i8]* @str_13, i64 0, i64 0))
la $a0, str_13
li $v0 4
syscall

# call void @putint(i32 %local_var_54)

move $a0 $s0
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_14, i64 0, i64 0))
la $a0, str_14
li $v0 4
syscall

# %local_var_60 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_4, i32 0, i32 0
la $k0, global_var_4
addi $s4, $k0, 0


# %local_var_61 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_1, i32 0, i32 0
la $k0, global_var_1
addi $s5, $k0, 0


# %local_var_62 = load i8, i8* %local_var_61
lb $s5, 0($s5)


# %local_var_63 = zext i8 %local_var_62 to i32
sb $s5, -4($sp)


# %local_var_64 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_1, i32 0, i32 1
la $k0, global_var_1
addi $s5, $k0, 1


# %local_var_65 = load i8, i8* %local_var_64
lb $s5, 0($s5)


# %local_var_66 = zext i8 %local_var_65 to i32
sb $s5, -8($sp)


# %local_var_67 = add i32 %local_var_63, %local_var_66
lw $k0, -4($sp)

lw $k1, -8($sp)

addu $s5, $k0, $k1


# %local_var_68 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_1, i32 0, i32 2
la $k0, global_var_1
addi $s6, $k0, 2


# %local_var_69 = load i8, i8* %local_var_68
lb $s6, 0($s6)


# %local_var_70 = zext i8 %local_var_69 to i32
sb $s6, -12($sp)


# %local_var_71 = add i32 %local_var_67, %local_var_70
lw $k1, -12($sp)

addu $s5, $s5, $k1


# %local_var_72 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_1, i32 0, i32 3
la $k0, global_var_1
addi $s6, $k0, 3


# %local_var_73 = load i8, i8* %local_var_72
lb $s6, 0($s6)


# %local_var_74 = zext i8 %local_var_73 to i32
sb $s6, -16($sp)


# %local_var_75 = add i32 %local_var_71, %local_var_74
lw $k1, -16($sp)

addu $s5, $s5, $k1


# %local_var_76 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_1, i32 0, i32 4
la $k0, global_var_1
addi $s6, $k0, 4


# %local_var_77 = load i8, i8* %local_var_76
lb $s6, 0($s6)


# %local_var_78 = zext i8 %local_var_77 to i32
sb $s6, -20($sp)


# %local_var_79 = add i32 %local_var_75, %local_var_78
lw $k1, -20($sp)

addu $s5, $s5, $k1


# %local_var_80 = trunc i32 %local_var_79 to i8
sb $s5, -21($sp)


# store i8 %local_var_80, i8* %local_var_60
lb $k0, -21($sp)

sb $k0, 0($s4)


# %local_var_81 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_4, i32 0, i32 0
la $k0, global_var_4
addi $s4, $k0, 0


# %local_var_82 = load i8, i8* %local_var_81
lb $s4, 0($s4)


# %local_var_83 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_4, i32 0, i32 0
la $k0, global_var_4
addi $s5, $k0, 0


# %local_var_84 = load i8, i8* %local_var_83
lb $s5, 0($s5)


# call void @putstr(i8* getelementptr inbounds ([22 x i8], [22 x i8]* @str_15, i64 0, i64 0))
la $a0, str_15
li $v0 4
syscall

# %local_var_86 = zext i8 %local_var_82 to i32
sb $s4, -28($sp)


# call void @putint(i32 %local_var_86)

lw $a0, -28($sp)

li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_16, i64 0, i64 0))
la $a0, str_16
li $v0 4
syscall

# %local_var_89 = zext i8 %local_var_84 to i32
sb $s5, -32($sp)


# call void @putch(i32 %local_var_89)

lw $a0, -32($sp)

li $v0 11
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_17, i64 0, i64 0))
la $a0, str_17
li $v0 4
syscall

# %local_var_92 = alloca i32
addi $t9, $sp, -36


# %local_var_93 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $t0, $k0, 0


# %local_var_94 = load i32, i32* %local_var_93
lw $k1, 0($t0)

sw $k1, -40($sp)


# %local_var_95 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_4, i32 0, i32 0
la $k0, global_var_4
addi $t0, $k0, 0


# %local_var_96 = load i8, i8* %local_var_95
lb $k1, 0($t0)

sb $k1, -41($sp)


# %local_var_97 = getelementptr inbounds [5 x i32], [5 x i32]* @global_var_3, i32 0, i32 0
la $k0, global_var_3
addi $k0, $k0, 0

sw $k0, -48($sp)


# %local_var_98 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_4, i32 0, i32 0
la $k0, global_var_4
addi $t0, $k0, 0


# %local_var_99 = call i32 @func_func_with_param(i32 %local_var_94, i8 %local_var_96, i32* %local_var_97, i8* %local_var_98)
sw $s6, -52($sp)

sw $t8, -56($sp)

sw $s2, -60($sp)

sw $t3, -64($sp)

sw $t2, -68($sp)

sw $s5, -72($sp)

sw $s0, -76($sp)

sw $t0, -80($sp)

sw $t1, -84($sp)

sw $s1, -88($sp)

sw $t5, -92($sp)

sw $t7, -96($sp)

sw $s3, -100($sp)

sw $t9, -104($sp)

sw $s4, -108($sp)

sw $t4, -112($sp)

sw $s7, -116($sp)

sw $t6, -120($sp)

sw $sp, -124($sp)

sw $ra, -128($sp)

lw $a1, -40($sp)

lb $a2, -41($sp)

lw $a3, -48($sp)

sw $t0, -144($sp)

addi $sp, $sp, -128

jal func_func_with_param

lw $ra, 0($sp)

lw $sp, 4($sp)

lw $s6, -52($sp)

lw $t8, -56($sp)

lw $s2, -60($sp)

lw $t3, -64($sp)

lw $t2, -68($sp)

lw $s5, -72($sp)

lw $s0, -76($sp)

lw $t0, -80($sp)

lw $t1, -84($sp)

lw $s1, -88($sp)

lw $t5, -92($sp)

lw $t7, -96($sp)

lw $s3, -100($sp)

lw $t9, -104($sp)

lw $s4, -108($sp)

lw $t4, -112($sp)

lw $s7, -116($sp)

lw $t6, -120($sp)

move $t0 $v0

# store i32 %local_var_99, i32* %local_var_92
sw $t0, 0($t9)


# %local_var_100 = alloca i32
addi $k0, $sp, -52

sw $k0, -56($sp)


# %local_var_101 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 0
la $k0, global_var_2
addi $t9, $k0, 0


# %local_var_102 = load i8, i8* %local_var_101
lb $t9, 0($t9)


# %local_var_103 = zext i8 %local_var_102 to i32
sb $t9, -60($sp)


# %local_var_104 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 1
la $k0, global_var_2
addi $t9, $k0, 1


# %local_var_105 = load i8, i8* %local_var_104
lb $t9, 0($t9)


# %local_var_106 = zext i8 %local_var_105 to i32
sb $t9, -64($sp)


# %local_var_107 = add i32 %local_var_103, %local_var_106
lw $k0, -60($sp)

lw $k1, -64($sp)

addu $t9, $k0, $k1


# %local_var_108 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 2
la $k0, global_var_2
addi $t0, $k0, 2


# %local_var_109 = load i8, i8* %local_var_108
lb $t0, 0($t0)


# %local_var_110 = zext i8 %local_var_109 to i32
sb $t0, -68($sp)


# %local_var_111 = add i32 %local_var_107, %local_var_110
lw $k1, -68($sp)

addu $t0, $t9, $k1


# %local_var_112 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 3
la $k0, global_var_2
addi $t9, $k0, 3


# %local_var_113 = load i8, i8* %local_var_112
lb $t9, 0($t9)


# %local_var_114 = zext i8 %local_var_113 to i32
sb $t9, -72($sp)


# %local_var_115 = add i32 %local_var_111, %local_var_114
lw $k1, -72($sp)

addu $t0, $t0, $k1


# %local_var_116 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 4
la $k0, global_var_2
addi $t9, $k0, 4


# %local_var_117 = load i8, i8* %local_var_116
lb $t9, 0($t9)


# %local_var_118 = zext i8 %local_var_117 to i32
sb $t9, -76($sp)


# %local_var_119 = add i32 %local_var_115, %local_var_118
lw $k1, -76($sp)

addu $t0, $t0, $k1


# store i32 %local_var_119, i32* %local_var_100
lw $k1, -56($sp)

sw $t0, 0($k1)


# %local_var_120 = alloca i8
addi $k0, $sp, -77

sw $k0, -84($sp)


# %local_var_121 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 0
la $k0, global_var_2
addi $t9, $k0, 0


# %local_var_122 = load i8, i8* %local_var_121
lb $t9, 0($t9)


# %local_var_123 = zext i8 %local_var_122 to i32
sb $t9, -88($sp)


# %local_var_124 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 1
la $k0, global_var_2
addi $t9, $k0, 1


# %local_var_125 = load i8, i8* %local_var_124
lb $t9, 0($t9)


# %local_var_126 = zext i8 %local_var_125 to i32
sb $t9, -92($sp)


# %local_var_127 = add i32 %local_var_123, %local_var_126
lw $k0, -88($sp)

lw $k1, -92($sp)

addu $t9, $k0, $k1


# %local_var_128 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 2
la $k0, global_var_2
addi $t0, $k0, 2


# %local_var_129 = load i8, i8* %local_var_128
lb $t0, 0($t0)


# %local_var_130 = zext i8 %local_var_129 to i32
sb $t0, -96($sp)


# %local_var_131 = add i32 %local_var_127, %local_var_130
lw $k1, -96($sp)

addu $t0, $t9, $k1


# %local_var_132 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 3
la $k0, global_var_2
addi $t9, $k0, 3


# %local_var_133 = load i8, i8* %local_var_132
lb $t9, 0($t9)


# %local_var_134 = zext i8 %local_var_133 to i32
sb $t9, -100($sp)


# %local_var_135 = add i32 %local_var_131, %local_var_134
lw $k1, -100($sp)

addu $t0, $t0, $k1


# %local_var_136 = getelementptr inbounds [5 x i8], [5 x i8]* @global_var_2, i32 0, i32 4
la $k0, global_var_2
addi $t9, $k0, 4


# %local_var_137 = load i8, i8* %local_var_136
lb $t9, 0($t9)


# %local_var_138 = zext i8 %local_var_137 to i32
sb $t9, -104($sp)


# %local_var_139 = add i32 %local_var_135, %local_var_138
lw $k1, -104($sp)

addu $t0, $t0, $k1


# %local_var_140 = trunc i32 %local_var_139 to i8
sb $t0, -105($sp)


# store i8 %local_var_140, i8* %local_var_120
lw $k1, -84($sp)

lb $k0, -105($sp)

sb $k0, 0($k1)


# %local_var_141 = load i32, i32* %local_var_100
lw $k0, -56($sp)

lw $k1, 0($k0)

sw $k1, -112($sp)


# %local_var_142 = load i8, i8* %local_var_120
lw $k0, -84($sp)

lb $t9, 0($k0)


# call void @putstr(i8* getelementptr inbounds ([22 x i8], [22 x i8]* @str_18, i64 0, i64 0))
la $a0, str_18
li $v0 4
syscall

# call void @putint(i32 %local_var_141)

lw $a0, -112($sp)

li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_19, i64 0, i64 0))
la $a0, str_19
li $v0 4
syscall

# %local_var_146 = zext i8 %local_var_142 to i32
sb $t9, -116($sp)


# call void @putch(i32 %local_var_146)

lw $a0, -116($sp)

li $v0 11
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_20, i64 0, i64 0))
la $a0, str_20
li $v0 4
syscall

# %local_var_149 = alloca i32
addi $k0, $sp, -120

sw $k0, -124($sp)


# %local_var_150 = zext i8 97 to i32
li $k0 97
sw $k0, -128($sp)


# %local_var_151 = add i32 %local_var_150, 10
li $k1 10
lw $k0, -128($sp)

addu $t0, $k0, $k1


# store i32 %local_var_151, i32* %local_var_149
lw $k1, -124($sp)

sw $t0, 0($k1)


# %local_var_152 = load i32, i32* %local_var_149
lw $k0, -124($sp)

lw $k1, 0($k0)

sw $k1, -132($sp)


# call void @putstr(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @str_21, i64 0, i64 0))
la $a0, str_21
li $v0 4
syscall

# call void @putint(i32 %local_var_152)

lw $a0, -132($sp)

li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_22, i64 0, i64 0))
la $a0, str_22
li $v0 4
syscall

# %local_var_156 = alloca i8
addi $k0, $sp, -133

sw $k0, -140($sp)


# %local_var_157 = zext i8 48 to i32
li $k0 48
sw $k0, -144($sp)


# %local_var_158 = sub i32 %local_var_157, 7
li $k1 7
lw $k0, -144($sp)

subu $t0, $k0, $k1


# %local_var_159 = trunc i32 %local_var_158 to i8
sb $t0, -145($sp)


# store i8 %local_var_159, i8* %local_var_156
lw $k1, -140($sp)

lb $k0, -145($sp)

sb $k0, 0($k1)


# %local_var_160 = load i8, i8* %local_var_156
lw $k0, -140($sp)

lb $k1, 0($k0)

sb $k1, -146($sp)


# %local_var_161 = load i8, i8* %local_var_156
lw $k0, -140($sp)

lb $k1, 0($k0)

sb $k1, -147($sp)


# call void @putstr(i8* getelementptr inbounds ([6 x i8], [6 x i8]* @str_23, i64 0, i64 0))
la $a0, str_23
li $v0 4
syscall

# %local_var_163 = zext i8 %local_var_160 to i32
lb $k0, -146($sp)

sw $k0, -152($sp)


# call void @putint(i32 %local_var_163)

lw $a0, -152($sp)

li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([12 x i8], [12 x i8]* @str_24, i64 0, i64 0))
la $a0, str_24
li $v0 4
syscall

# %local_var_166 = zext i8 %local_var_161 to i32
lb $k0, -147($sp)

sw $k0, -156($sp)


# call void @putch(i32 %local_var_166)

lw $a0, -156($sp)

li $v0 11
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_25, i64 0, i64 0))
la $a0, str_25
li $v0 4
syscall

# ret i32 0
li $v0 0
jr $ra

exit:
li $v0 10
syscall