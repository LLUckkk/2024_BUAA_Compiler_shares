.data
global_var_0: .word 10

global_var_1: .word 90

global_var_2: .word 1, 2, 3

global_var_3: .word 5

global_var_4: .word 114514

global_var_5: .word 0:3

global_var_6: .word 0

global_var_7: .byte 0

global_var_8: .byte 97, 98, 99, 0, 0

global_var_9: .byte 120, 121, 122, 0, 0, 0

str_0: .asciiz "21374067\n"

str_1: .asciiz "\n"

str_2: .asciiz "i: "

str_3: .asciiz "\n"

str_4: .asciiz "\n"

str_5: .asciiz "\n"




.text
jal main

j exit

func_g:
block_0:

# %local_var_0 = getelementptr inbounds i32, i32* %param_0, i32 0
addi $t0, $a1, 0


# %local_var_1 = load i32, i32* %local_var_0
lw $t0, 0($t0)


# %local_var_2 = getelementptr inbounds i32, i32* %param_0, i32 1
addi $t1, $a1, 4


# %local_var_3 = load i32, i32* %local_var_2
lw $t1, 0($t1)


# %local_var_4 = getelementptr inbounds i32, i32* %param_0, i32 0
addi $t2, $a1, 0


# %local_var_5 = load i32, i32* %local_var_4
lw $t2, 0($t2)


# %local_var_6 = sub i32 0, %local_var_5
li $k0 0
subu $t2, $k0, $t2


# %local_var_7 = add i32 %local_var_3, %local_var_6
addu $t1, $t1, $t2


# %local_var_8 = getelementptr inbounds i32, i32* %param_0, i32 %local_var_7
sll $k1, $t1, 2

addu $t1, $a1, $k1


# %local_var_9 = load i32, i32* %local_var_8
lw $t1, 0($t1)


# %local_var_10 = add i32 %local_var_1, %local_var_9
addu $t0, $t0, $t1


# ret i32 %local_var_10
move $v0 $t0
jr $ra

func_foo:
block_1:

# ret i8 111
li $v0 111
jr $ra

func_fooo:
block_2:

# ret void
jr $ra

func_func:
block_3:

# %local_var_0 = load i32, i32* @global_var_6
la $k0, global_var_6
lw $t0, 0($k0)


# %local_var_1 = add i32 %local_var_0, 1
li $k1 1
addu $t0, $t0, $k1


# store i32 %local_var_1, i32* @global_var_6
la $k1, global_var_6
sw $t0, 0($k1)


# ret i32 1
li $v0 1
jr $ra

main:

# call void @putstr(i8* getelementptr inbounds ([10 x i8], [10 x i8]* @str_0, i64 0, i64 0))
la $a0, str_0
li $v0 4
syscall

# %local_var_5 = sub i32 0, 10
li $t1 -10

# %local_var_8 = add i32 %local_var_5, 5
li $k1 5
addu $t2, $t1, $k1


# %local_var_9 = mul i32 %local_var_8, 2
li $k1 2
mult $t2 $k1
mflo $t2

# %local_var_10 = sdiv i32 %local_var_9, 1
li $k1 1
div $t2 $k1
mflo $t2

# %local_var_11 = add i32 %local_var_10, 0
li $k1 0
addu $t2, $t2, $k1


# %local_var_13 = icmp slt i32 %local_var_5, 20
li $k1 20
slt $t3, $t1, $k1

# br i1 %local_var_13, label %block_4, label %block_7
bne $t3, $zero, block_4
j block_7

block_4:

# %local_var_27 = sub i32 %local_var_5, 1
li $k1 1
subu $t0, $t1, $k1


# move i32* %local_var_153 %local_var_27

# br label %block_5
j block_5

block_5:

# call void @putint(i32 %local_var_153)

move $a0 $t0
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_1, i64 0, i64 0))
la $a0, str_1
li $v0 4
syscall

# move i32* %local_var_146 0
li $t0 0

# br label %block_13
j block_13

block_6:

# %local_var_19 = icmp sgt i32 %local_var_5, 0
li $k1 0
sgt $t0, $t1, $k1

# br i1 %local_var_19, label %block_10, label %block_60
bne $t0, $zero, block_10
j block_60

block_7:

# %local_var_14 = call i32 @func_func()
sw $t4, -4($sp)

sw $t3, -8($sp)

sw $t0, -12($sp)

sw $t1, -16($sp)

sw $t2, -20($sp)

sw $sp, -24($sp)

sw $ra, -28($sp)

addi $sp, $sp, -28

jal func_func

lw $ra, 0($sp)

lw $sp, 4($sp)

lw $t4, -4($sp)

lw $t3, -8($sp)

lw $t0, -12($sp)

lw $t1, -16($sp)

lw $t2, -20($sp)

move $t0 $v0

# %local_var_15 = icmp eq i32 0, %local_var_14
li $k0 0
seq $t0, $k0, $t0

# %local_var_16 = zext i1 %local_var_15 to i32
sw $t0, -4($sp)


# %local_var_17 = icmp ne i32 %local_var_16, 0
lw $k0, -4($sp)

li $k1 0
sne $t0, $k0, $k1

# br i1 %local_var_17, label %block_4, label %block_6
bne $t0, $zero, block_4
j block_6

block_8:

# %local_var_23 = add i32 %local_var_5, 1
li $k1 1
addu $t0, $t1, $k1


# move i32* %local_var_154 %local_var_23

# br label %block_9
j block_9

block_60:

# move i32* %local_var_154 %local_var_5
move $t0 $t1

# br label %block_9
j block_9

block_61:

# move i32* %local_var_154 %local_var_5
move $t0 $t1

# br label %block_9
j block_9

block_9:

# move i32* %local_var_153 %local_var_154

# br label %block_5
j block_5

block_10:

# %local_var_20 = call i32 @func_func()
sw $t4, -8($sp)

sw $t3, -12($sp)

sw $t0, -16($sp)

sw $t1, -20($sp)

sw $t2, -24($sp)

sw $sp, -28($sp)

sw $ra, -32($sp)

addi $sp, $sp, -32

jal func_func

lw $ra, 0($sp)

lw $sp, 4($sp)

lw $t4, -8($sp)

lw $t3, -12($sp)

lw $t0, -16($sp)

lw $t1, -20($sp)

lw $t2, -24($sp)

move $t0 $v0

# %local_var_21 = icmp ne i32 %local_var_20, 0
li $k1 0
sne $t0, $t0, $k1

# br i1 %local_var_21, label %block_8, label %block_61
bne $t0, $zero, block_8
j block_61

block_11:

# call void @putstr(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @str_2, i64 0, i64 0))
la $a0, str_2
li $v0 4
syscall

# call void @putint(i32 %local_var_146)

move $a0 $t0
li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_3, i64 0, i64 0))
la $a0, str_3
li $v0 4
syscall

# br label %block_14
j block_14

block_12:

# %local_var_39 = alloca [3 x i32]
addi $k0, $sp, -16

sw $k0, -20($sp)


# %local_var_40 = getelementptr inbounds [3 x i32], [3 x i32]* %local_var_39, i32 0, i32 0
lw $k0, -20($sp)

addi $t0, $k0, 0


# store i32 1, i32* %local_var_40
li $k0 1
sw $k0, 0($t0)


# %local_var_41 = getelementptr inbounds [3 x i32], [3 x i32]* %local_var_39, i32 0, i32 1
lw $k0, -20($sp)

addi $t0, $k0, 4


# store i32 2, i32* %local_var_41
li $k0 2
sw $k0, 0($t0)


# %local_var_42 = getelementptr inbounds [3 x i32], [3 x i32]* %local_var_39, i32 0, i32 2
lw $k0, -20($sp)

addi $t0, $k0, 8


# store i32 3, i32* %local_var_42
li $k0 3
sw $k0, 0($t0)


# %local_var_44 = call i32 @func_func()
sw $t4, -24($sp)

sw $t3, -28($sp)

sw $t0, -32($sp)

sw $t1, -36($sp)

sw $t2, -40($sp)

sw $sp, -44($sp)

sw $ra, -48($sp)

addi $sp, $sp, -48

jal func_func

lw $ra, 0($sp)

lw $sp, 4($sp)

lw $t4, -24($sp)

lw $t3, -28($sp)

lw $t0, -32($sp)

lw $t1, -36($sp)

lw $t2, -40($sp)

move $t0 $v0

# %local_var_46 = call i32 @getint()

li $v0 5
syscall
move $t1 $v0

# %local_var_47 = getelementptr inbounds [3 x i32], [3 x i32]* %local_var_39, i32 0, i32 0
lw $k0, -20($sp)

addi $t2, $k0, 0


# %local_var_48 = call i32 @func_g(i32* %local_var_47)
sw $t4, -24($sp)

sw $t3, -28($sp)

sw $t0, -32($sp)

sw $t1, -36($sp)

sw $t2, -40($sp)

sw $sp, -44($sp)

sw $ra, -48($sp)

move $a1 $t2
addi $sp, $sp, -48

jal func_g

lw $ra, 0($sp)

lw $sp, 4($sp)

lw $t4, -24($sp)

lw $t3, -28($sp)

lw $t0, -32($sp)

lw $t1, -36($sp)

lw $t2, -40($sp)

move $t2 $v0

# %local_var_49 = call i32 @getchar()

li $v0 12
syscall
move $t3 $v0

# %local_var_50 = trunc i32 %local_var_49 to i8
sb $t3, -21($sp)


# move i8* %local_var_151 97
li $t0 97

# br label %block_17
j block_17

block_13:

# %local_var_32 = icmp slt i32 %local_var_146, 6
li $k1 6
slt $t1, $t0, $k1

# br i1 %local_var_32, label %block_11, label %block_12
bne $t1, $zero, block_11
j block_12

block_14:

# %local_var_38 = add i32 %local_var_146, 1
li $k1 1
addu $t0, $t0, $k1


# move i32* %local_var_146 %local_var_38

# br label %block_13
j block_13

block_15:

# %local_var_58 = zext i8* %local_var_151 to i32
sw $t0, -28($sp)


# %local_var_59 = add i32 %local_var_58, 1
li $k1 1
lw $k0, -28($sp)

addu $t1, $k0, $k1


# %local_var_60 = trunc i32 %local_var_59 to i8
sb $t1, -29($sp)


# %local_var_62 = zext i8 %local_var_60 to i32
lb $k0, -29($sp)

sw $k0, -36($sp)


# %local_var_63 = zext i8 120 to i32
li $k0 120
sw $k0, -40($sp)


# %local_var_64 = icmp eq i32 %local_var_62, %local_var_63
lw $k0, -36($sp)

lw $k1, -40($sp)

seq $t1, $k0, $k1

# br i1 %local_var_64, label %block_20, label %block_21
bne $t1, $zero, block_20
j block_21

block_16:

# move i8* %local_var_150 97
li $t0 97

# br label %block_22
j block_22

block_17:

# %local_var_52 = zext i8* %local_var_151 to i32
sw $t0, -44($sp)


# %local_var_53 = srem i32 127, 128
li $t1 127

# %local_var_54 = icmp slt i32 %local_var_52, %local_var_53
lw $k0, -44($sp)

slt $t1, $k0, $t1

# br i1 %local_var_54, label %block_15, label %block_18
bne $t1, $zero, block_15
j block_18

block_18:

# %local_var_56 = icmp ne i32 %local_var_48, 0
li $k1 0
sne $t1, $t2, $k1

# br i1 %local_var_56, label %block_15, label %block_16
bne $t1, $zero, block_15
j block_16

block_19:

# %local_var_66 = zext i8 %local_var_60 to i32
lb $k0, -29($sp)

sw $k0, -48($sp)


# %local_var_67 = add i32 %local_var_66, 1
li $k1 1
lw $k0, -48($sp)

addu $t0, $k0, $k1


# %local_var_68 = trunc i32 %local_var_67 to i8
sb $t0, -49($sp)


# move i8* %local_var_151 %local_var_68
lb $t0, -49($sp)


# br label %block_17
j block_17

block_20:

# br label %block_16
j block_16

block_21:

# br label %block_19
j block_19

block_22:

# %local_var_70 = zext i8* %local_var_150 to i32
sw $t0, -56($sp)


# %local_var_71 = add i32 %local_var_70, 1
li $k1 1
lw $k0, -56($sp)

addu $t0, $k0, $k1


# %local_var_72 = trunc i32 %local_var_71 to i8
sb $t0, -57($sp)


# %local_var_74 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -64($sp)


# %local_var_75 = zext i8 120 to i32
li $k0 120
sw $k0, -68($sp)


# %local_var_76 = icmp eq i32 %local_var_74, %local_var_75
lw $k0, -64($sp)

lw $k1, -68($sp)

seq $t0, $k0, $k1

# br i1 %local_var_76, label %block_25, label %block_26
bne $t0, $zero, block_25
j block_26

block_23:

# %local_var_82 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -72($sp)


# call void @putch(i32 %local_var_82)

lw $a0, -72($sp)

li $v0 11
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_4, i64 0, i64 0))
la $a0, str_4
li $v0 4
syscall

# %local_var_86 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -76($sp)


# call void @putint(i32 %local_var_86)

lw $a0, -76($sp)

li $v0 1
syscall

# call void @putstr(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @str_5, i64 0, i64 0))
la $a0, str_5
li $v0 4
syscall

# %local_var_90 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -80($sp)


# %local_var_91 = icmp sgt i32 %local_var_90, 0
lw $k0, -80($sp)

li $k1 0
sgt $t4, $k0, $k1

# br i1 %local_var_91, label %block_29, label %block_28
bne $t4, $zero, block_29
j block_28

block_24:

# %local_var_78 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -84($sp)


# %local_var_79 = add i32 %local_var_78, 1
li $k1 1
lw $k0, -84($sp)

addu $t0, $k0, $k1


# %local_var_80 = trunc i32 %local_var_79 to i8
sb $t0, -85($sp)


# move i8* %local_var_150 %local_var_80
lb $t0, -85($sp)


# br label %block_22
j block_22

block_25:

# br label %block_23
j block_23

block_26:

# br label %block_24
j block_24

block_27:

# move i8* %local_var_148 %local_var_72
lb $t0, -57($sp)


# br label %block_36
j block_36

block_28:

# ret i32 0
li $v0 0
jr $ra

block_29:

# %local_var_93 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -92($sp)


# %local_var_94 = icmp slt i32 %local_var_93, 0
lw $k0, -92($sp)

li $k1 0
slt $t0, $k0, $k1

# br i1 %local_var_94, label %block_30, label %block_28
bne $t0, $zero, block_30
j block_28

block_30:

# %local_var_96 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -96($sp)


# %local_var_97 = icmp sle i32 %local_var_96, 0
lw $k0, -96($sp)

li $k1 0
sle $t0, $k0, $k1

# br i1 %local_var_97, label %block_31, label %block_28
bne $t0, $zero, block_31
j block_28

block_31:

# %local_var_99 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -100($sp)


# %local_var_100 = icmp sge i32 %local_var_99, 0
lw $k0, -100($sp)

li $k1 0
sge $t0, $k0, $k1

# br i1 %local_var_100, label %block_32, label %block_28
bne $t0, $zero, block_32
j block_28

block_32:

# %local_var_102 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -104($sp)


# %local_var_103 = icmp ne i32 %local_var_102, 0
lw $k0, -104($sp)

li $k1 0
sne $t0, $k0, $k1

# br i1 %local_var_103, label %block_33, label %block_28
bne $t0, $zero, block_33
j block_28

block_33:

# %local_var_105 = zext i8 %local_var_72 to i32
lb $k0, -57($sp)

sw $k0, -108($sp)


# %local_var_106 = icmp eq i32 %local_var_105, 0
lw $k0, -108($sp)

li $k1 0
seq $t0, $k0, $k1

# br i1 %local_var_106, label %block_27, label %block_28
bne $t0, $zero, block_27
j block_28

block_34:

# %local_var_113 = zext i8* %local_var_148 to i32
sw $t0, -112($sp)


# %local_var_114 = add i32 %local_var_113, 1
li $k1 1
lw $k0, -112($sp)

addu $t1, $k0, $k1


# %local_var_115 = trunc i32 %local_var_114 to i8
sb $t1, -113($sp)


# %local_var_117 = zext i8 %local_var_115 to i32
lb $k0, -113($sp)

sw $k0, -120($sp)


# %local_var_118 = zext i8 120 to i32
li $k0 120
sw $k0, -124($sp)


# %local_var_119 = icmp eq i32 %local_var_117, %local_var_118
lw $k0, -120($sp)

lw $k1, -124($sp)

seq $t1, $k0, $k1

# br i1 %local_var_119, label %block_38, label %block_39
bne $t1, $zero, block_38
j block_39

block_62:

# move i8* %local_var_149 %local_var_148
move $t1 $t0

# br label %block_35
j block_35

block_35:

# br label %block_40
j block_40

block_36:

# %local_var_108 = zext i8* %local_var_148 to i32
sw $t0, -128($sp)


# %local_var_109 = icmp eq i32 0, %local_var_108
li $k0 0
lw $k1, -128($sp)

seq $t1, $k0, $k1

# %local_var_110 = zext i1 %local_var_109 to i32
sw $t1, -132($sp)


# %local_var_111 = icmp ne i32 %local_var_110, 0
lw $k0, -132($sp)

li $k1 0
sne $t1, $k0, $k1

# br i1 %local_var_111, label %block_34, label %block_62
bne $t1, $zero, block_34
j block_62

block_37:

# %local_var_121 = zext i8 %local_var_115 to i32
lb $k0, -113($sp)

sw $k0, -136($sp)


# %local_var_122 = add i32 %local_var_121, 1
li $k1 1
lw $k0, -136($sp)

addu $t0, $k0, $k1


# %local_var_123 = trunc i32 %local_var_122 to i8
sb $t0, -137($sp)


# move i8* %local_var_148 %local_var_123
lb $t0, -137($sp)


# br label %block_36
j block_36

block_38:

# move i8* %local_var_149 %local_var_115
lb $t1, -113($sp)


# br label %block_35
j block_35

block_39:

# br label %block_37
j block_37

block_40:

# br label %block_40
j block_40

exit:
li $v0 10
syscall