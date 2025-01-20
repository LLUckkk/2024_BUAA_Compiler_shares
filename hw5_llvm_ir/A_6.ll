; ModuleID = 'public_A_6.c'
source_filename = "public_A_6.c"
target datalayout = "e-m:x-p:32:32-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32-a:0:32-S32"
target triple = "i686-pc-windows-msvc19.33.31629"

$"??_C@_09JEDAENPD@22373040?6?$AA@" = comdat any

$"??_C@_03PJCJOCBM@?$CFc?6?$AA@" = comdat any

$"??_C@_02HAOIJKIC@?$CFc?$AA@" = comdat any

$"??_C@_02DPKJAMEF@?$CFd?$AA@" = comdat any

$"??_C@_0BD@GEKGCHDH@Input?5integer?3?5?$CFd?6?$AA@" = comdat any

$"??_C@_0BF@MCEJOAMF@Input?5character?3?5?$CFc?6?$AA@" = comdat any

$"??_C@_0O@BMOKODCD@i?5is?54?5or?59?$CB?6?$AA@" = comdat any

$"??_C@_09DCAKMDKC@j?5is?532?$CB?6?$AA@" = comdat any

$"??_C@_0BL@IEINIPOD@Sum?5of?5array?5elements?3?5?$CFd?6?$AA@" = comdat any

$"??_C@_0BA@FLKABJIH@Test?5finished?$CB?6?$AA@" = comdat any

@MAX_SIZE = dso_local constant i32 10, align 4
@str = dso_local global <{ i8, i8, [8 x i8] }> <{ i8 51, i8 39, [8 x i8] zeroinitializer }>, align 1
@global_var = dso_local global i32 0, align 4
@"??_C@_09JEDAENPD@22373040?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [10 x i8] c"22373040\0A\00", comdat, align 1
@"??_C@_03PJCJOCBM@?$CFc?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [4 x i8] c"%c\0A\00", comdat, align 1
@"??_C@_02HAOIJKIC@?$CFc?$AA@" = linkonce_odr dso_local unnamed_addr constant [3 x i8] c"%c\00", comdat, align 1
@"??_C@_02DPKJAMEF@?$CFd?$AA@" = linkonce_odr dso_local unnamed_addr constant [3 x i8] c"%d\00", comdat, align 1
@"??_C@_0BD@GEKGCHDH@Input?5integer?3?5?$CFd?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [19 x i8] c"Input integer: %d\0A\00", comdat, align 1
@"??_C@_0BF@MCEJOAMF@Input?5character?3?5?$CFc?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [21 x i8] c"Input character: %c\0A\00", comdat, align 1
@__const.main.str = private unnamed_addr constant [12 x i8] c"qwertyuiop\0A\00", align 1
@__const.main._str = private unnamed_addr constant [10 x i8] c"str\00\00\00\00\00\00\00", align 1
@"??_C@_0O@BMOKODCD@i?5is?54?5or?59?$CB?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [14 x i8] c"i is 4 or 9!\0A\00", comdat, align 1
@"??_C@_09DCAKMDKC@j?5is?532?$CB?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [10 x i8] c"j is 32!\0A\00", comdat, align 1
@"??_C@_0BL@IEINIPOD@Sum?5of?5array?5elements?3?5?$CFd?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [27 x i8] c"Sum of array elements: %d\0A\00", comdat, align 1
@"??_C@_0BA@FLKABJIH@Test?5finished?$CB?6?$AA@" = linkonce_odr dso_local unnamed_addr constant [16 x i8] c"Test finished!\0A\00", comdat, align 1

; Function Attrs: noinline nounwind optnone
define dso_local i32 @add(i32 noundef %0, i32 noundef %1) #0 {
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  store i32 %1, i32* %3, align 4
  store i32 %0, i32* %4, align 4
  %5 = load i32, i32* %4, align 4
  %6 = load i32, i32* %3, align 4
  %7 = add nsw i32 %5, %6
  ret i32 %7
}

; Function Attrs: noinline nounwind optnone
define dso_local i32 @calculate(i32 noundef %0, i32* noundef %1) #0 {
  %3 = alloca i32, align 4
  %4 = alloca i32*, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  store i32* %1, i32** %4, align 4
  store i32 %0, i32* %5, align 4
  %7 = load i32*, i32** %4, align 4
  %8 = getelementptr inbounds i32, i32* %7, i32 0
  %9 = load i32, i32* %8, align 4
  %10 = load i32, i32* %5, align 4
  %11 = call i32 @add(i32 noundef %10, i32 noundef %9)
  %12 = load i32, i32* %5, align 4
  %13 = load i32*, i32** %4, align 4
  %14 = getelementptr inbounds i32, i32* %13, i32 1
  %15 = load i32, i32* %14, align 4
  %16 = sub nsw i32 %12, %15
  %17 = mul nsw i32 %11, %16
  %18 = load i32*, i32** %4, align 4
  %19 = getelementptr inbounds i32, i32* %18, i32 2
  %20 = load i32, i32* %19, align 4
  %21 = sdiv i32 %17, %20
  %22 = load i32, i32* %5, align 4
  %23 = srem i32 %21, %22
  %24 = sub nsw i32 %23, -3
  %25 = add nsw i32 %24, 6
  store i32 %25, i32* %6, align 4
  %26 = load i32, i32* %6, align 4
  %27 = icmp sle i32 %26, 5
  br i1 %27, label %28, label %29

28:                                               ; preds = %2
  store i32 1, i32* %3, align 4
  br label %30

29:                                               ; preds = %2
  store i32 0, i32* %3, align 4
  br label %30

30:                                               ; preds = %29, %28
  %31 = load i32, i32* %3, align 4
  ret i32 %31
}

; Function Attrs: noinline nounwind optnone
define dso_local void @printName() #0 {
  %1 = load i32, i32* @global_var, align 4
  %2 = add nsw i32 %1, 1
  store i32 %2, i32* @global_var, align 4
  %3 = load i32, i32* @global_var, align 4
  %4 = icmp ne i32 %3, 0
  br i1 %4, label %5, label %7

5:                                                ; preds = %0
  %6 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([10 x i8], [10 x i8]* @"??_C@_09JEDAENPD@22373040?6?$AA@", i32 0, i32 0))
  br label %7

7:                                                ; preds = %5, %0
  ret void
}

declare dso_local i32 @printf(i8* noundef, ...) #1

; Function Attrs: noinline nounwind optnone
define dso_local void @print(i8 noundef signext %0) #0 {
  %2 = alloca i8, align 1
  store i8 %0, i8* %2, align 1
  %3 = load i8, i8* %2, align 1
  %4 = sext i8 %3 to i32
  %5 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([4 x i8], [4 x i8]* @"??_C@_03PJCJOCBM@?$CFc?6?$AA@", i32 0, i32 0), i32 noundef %4)
  ret void
}

; Function Attrs: noinline nounwind optnone
define dso_local signext i8 @get_first(i8* noundef %0) #0 {
  %2 = alloca i8*, align 4
  store i8* %0, i8** %2, align 4
  %3 = load i8*, i8** %2, align 4
  %4 = getelementptr inbounds i8, i8* %3, i32 0
  %5 = load i8, i8* %4, align 1
  ret i8 %5
}

; Function Attrs: noinline nounwind optnone
define dso_local i32 @getchar() #0 {
  %1 = alloca i8, align 1
  %2 = call i32 (i8*, ...) @scanf(i8* noundef getelementptr inbounds ([3 x i8], [3 x i8]* @"??_C@_02HAOIJKIC@?$CFc?$AA@", i32 0, i32 0), i8* noundef %1)
  %3 = load i8, i8* %1, align 1
  %4 = sext i8 %3 to i32
  ret i32 %4
}

declare dso_local i32 @scanf(i8* noundef, ...) #1

; Function Attrs: noinline nounwind optnone
define dso_local i32 @getint() #0 {
  %1 = alloca i32, align 4
  %2 = call i32 (i8*, ...) @scanf(i8* noundef getelementptr inbounds ([3 x i8], [3 x i8]* @"??_C@_02DPKJAMEF@?$CFd?$AA@", i32 0, i32 0), i32* noundef %1)
  br label %3

3:                                                ; preds = %6, %0
  %4 = call i32 @getchar()
  %5 = icmp ne i32 %4, 10
  br i1 %5, label %6, label %7

6:                                                ; preds = %3
  br label %3, !llvm.loop !4

7:                                                ; preds = %3
  %8 = load i32, i32* %1, align 4
  ret i32 %8
}

; Function Attrs: noinline nounwind optnone
define dso_local i32 @main() #0 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  %4 = alloca i8, align 1
  %5 = alloca i32, align 4
  %6 = alloca [10 x i32], align 4
  %7 = alloca [12 x i8], align 1
  %8 = alloca [10 x i8], align 1
  %9 = alloca i32, align 4
  %10 = alloca [20 x i32], align 4
  %11 = alloca i32, align 4
  %12 = alloca i32, align 4
  store i32 0, i32* %1, align 4
  call void @printName()
  store i32 0, i32* %2, align 4
  store i32 8, i32* %3, align 4
  %13 = call i32 @getint()
  store i32 %13, i32* %2, align 4
  %14 = call i32 @getchar()
  %15 = trunc i32 %14 to i8
  store i8 %15, i8* %4, align 1
  %16 = load i32, i32* %2, align 4
  %17 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([19 x i8], [19 x i8]* @"??_C@_0BD@GEKGCHDH@Input?5integer?3?5?$CFd?6?$AA@", i32 0, i32 0), i32 noundef %16)
  %18 = load i8, i8* %4, align 1
  %19 = sext i8 %18 to i32
  %20 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([21 x i8], [21 x i8]* @"??_C@_0BF@MCEJOAMF@Input?5character?3?5?$CFc?6?$AA@", i32 0, i32 0), i32 noundef %19)
  store i32 5, i32* %5, align 4
  %21 = bitcast [12 x i8]* %7 to i8*
  call void @llvm.memcpy.p0i8.p0i8.i32(i8* align 1 %21, i8* align 1 getelementptr inbounds ([12 x i8], [12 x i8]* @__const.main.str, i32 0, i32 0), i32 12, i1 false)
  %22 = bitcast [10 x i8]* %8 to i8*
  call void @llvm.memcpy.p0i8.p0i8.i32(i8* align 1 %22, i8* align 1 getelementptr inbounds ([10 x i8], [10 x i8]* @__const.main._str, i32 0, i32 0), i32 10, i1 false)
  store i32 0, i32* %2, align 4
  br label %23

23:                                               ; preds = %67, %0
  %24 = load i32, i32* %2, align 4
  %25 = icmp slt i32 %24, 10
  br i1 %25, label %26, label %70

26:                                               ; preds = %23
  %27 = load i32, i32* %2, align 4
  %28 = load i32, i32* %2, align 4
  %29 = getelementptr inbounds [10 x i32], [10 x i32]* %6, i32 0, i32 %28
  store i32 %27, i32* %29, align 4
  %30 = load i32, i32* %2, align 4
  %31 = icmp eq i32 %30, 4
  br i1 %31, label %32, label %37

32:                                               ; preds = %26
  %33 = load i32, i32* %2, align 4
  %34 = load i8, i8* %4, align 1
  %35 = sext i8 %34 to i32
  %36 = icmp slt i32 %33, %35
  br i1 %36, label %40, label %37

37:                                               ; preds = %32, %26
  %38 = load i32, i32* %2, align 4
  %39 = icmp sge i32 %38, 9
  br i1 %39, label %40, label %58

40:                                               ; preds = %37, %32
  %41 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([14 x i8], [14 x i8]* @"??_C@_0O@BMOKODCD@i?5is?54?5or?59?$CB?6?$AA@", i32 0, i32 0))
  store i32 1, i32* %9, align 4
  br label %42

42:                                               ; preds = %53, %40
  %43 = load i32, i32* %9, align 4
  %44 = icmp sgt i32 %43, 100
  br i1 %44, label %45, label %46

45:                                               ; preds = %42
  br label %57

46:                                               ; preds = %42
  %47 = load i32, i32* %9, align 4
  %48 = icmp ne i32 %47, 32
  br i1 %48, label %49, label %50

49:                                               ; preds = %46
  br label %53

50:                                               ; preds = %46
  br label %51

51:                                               ; preds = %50
  %52 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([10 x i8], [10 x i8]* @"??_C@_09DCAKMDKC@j?5is?532?$CB?6?$AA@", i32 0, i32 0))
  br label %53

53:                                               ; preds = %51, %49
  %54 = load i32, i32* %9, align 4
  %55 = load i32, i32* %9, align 4
  %56 = add nsw i32 %54, %55
  store i32 %56, i32* %9, align 4
  br label %42

57:                                               ; preds = %45
  br label %58

58:                                               ; preds = %57, %37
  %59 = load i32, i32* %2, align 4
  %60 = srem i32 %59, 2
  %61 = icmp eq i32 %60, 0
  br i1 %61, label %62, label %65

62:                                               ; preds = %58
  br label %63

63:                                               ; preds = %62
  br label %64

64:                                               ; preds = %63
  br label %66

65:                                               ; preds = %58
  br label %66

66:                                               ; preds = %65, %64
  br label %67

67:                                               ; preds = %66
  %68 = load i32, i32* %2, align 4
  %69 = add nsw i32 %68, 1
  store i32 %69, i32* %2, align 4
  br label %23, !llvm.loop !6

70:                                               ; preds = %23
  %71 = bitcast [20 x i32]* %10 to i8*
  call void @llvm.memset.p0i8.i32(i8* align 4 %71, i8 0, i32 80, i1 false)
  %72 = bitcast i8* %71 to <{ i32, i32, i32, [17 x i32] }>*
  %73 = getelementptr inbounds <{ i32, i32, i32, [17 x i32] }>, <{ i32, i32, i32, [17 x i32] }>* %72, i32 0, i32 0
  store i32 3, i32* %73, align 4
  %74 = getelementptr inbounds <{ i32, i32, i32, [17 x i32] }>, <{ i32, i32, i32, [17 x i32] }>* %72, i32 0, i32 1
  store i32 2, i32* %74, align 4
  %75 = getelementptr inbounds <{ i32, i32, i32, [17 x i32] }>, <{ i32, i32, i32, [17 x i32] }>* %72, i32 0, i32 2
  store i32 1, i32* %75, align 4
  store i32 0, i32* %11, align 4
  store i32 0, i32* %12, align 4
  br label %76

76:                                               ; preds = %89, %70
  %77 = load i32, i32* %12, align 4
  %78 = icmp slt i32 %77, 10
  br i1 %78, label %79, label %92

79:                                               ; preds = %76
  %80 = load i32, i32* %12, align 4
  %81 = icmp slt i32 %80, 3
  br i1 %81, label %82, label %88

82:                                               ; preds = %79
  %83 = load i32, i32* %11, align 4
  %84 = load i32, i32* %12, align 4
  %85 = getelementptr inbounds [20 x i32], [20 x i32]* %10, i32 0, i32 %84
  %86 = load i32, i32* %85, align 4
  %87 = add nsw i32 %83, %86
  store i32 %87, i32* %11, align 4
  br label %88

88:                                               ; preds = %82, %79
  br label %89

89:                                               ; preds = %88
  %90 = load i32, i32* %12, align 4
  %91 = add nsw i32 %90, 1
  store i32 %91, i32* %12, align 4
  br label %76, !llvm.loop !7

92:                                               ; preds = %76
  %93 = load i32, i32* %11, align 4
  %94 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([27 x i8], [27 x i8]* @"??_C@_0BL@IEINIPOD@Sum?5of?5array?5elements?3?5?$CFd?6?$AA@", i32 0, i32 0), i32 noundef %93)
  %95 = getelementptr inbounds [10 x i32], [10 x i32]* %6, i32 0, i32 0
  %96 = load i32, i32* %2, align 4
  %97 = call i32 @calculate(i32 noundef %96, i32* noundef %95)
  %98 = icmp ne i32 %97, 0
  br i1 %98, label %102, label %99

99:                                               ; preds = %92
  %100 = getelementptr inbounds [12 x i8], [12 x i8]* %7, i32 0, i32 0
  %101 = call signext i8 @get_first(i8* noundef %100)
  call void @print(i8 noundef signext %101)
  br label %102

102:                                              ; preds = %99, %92
  %103 = call i32 (i8*, ...) @printf(i8* noundef getelementptr inbounds ([16 x i8], [16 x i8]* @"??_C@_0BA@FLKABJIH@Test?5finished?$CB?6?$AA@", i32 0, i32 0))
  ret i32 0
}

; Function Attrs: argmemonly nofree nounwind willreturn
declare void @llvm.memcpy.p0i8.p0i8.i32(i8* noalias nocapture writeonly, i8* noalias nocapture readonly, i32, i1 immarg) #2

; Function Attrs: argmemonly nofree nounwind willreturn writeonly
declare void @llvm.memset.p0i8.i32(i8* nocapture writeonly, i8, i32, i1 immarg) #3

attributes #0 = { noinline nounwind optnone "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="pentium4" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="pentium4" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #2 = { argmemonly nofree nounwind willreturn }
attributes #3 = { argmemonly nofree nounwind willreturn writeonly }

!llvm.module.flags = !{!0, !1, !2}
!llvm.ident = !{!3}

!0 = !{i32 1, !"NumRegisterParameters", i32 0}
!1 = !{i32 1, !"wchar_size", i32 2}
!2 = !{i32 7, !"frame-pointer", i32 2}
!3 = !{!"clang version 14.0.5"}
!4 = distinct !{!4, !5}
!5 = !{!"llvm.loop.mustprogress"}
!6 = distinct !{!6, !5}
!7 = distinct !{!7, !5}
