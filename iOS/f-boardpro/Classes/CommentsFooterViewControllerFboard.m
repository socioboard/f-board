//
//  PAPPhotoDetailsFooterView.m
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/16/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import "CommentsFooterViewControllerFboard.h"


@interface CommentsFooterViewControllerFboard ()
@property (nonatomic, strong) UIView *mainView;
@end

@implementation CommentsFooterViewControllerFboard

@synthesize commentField;
@synthesize mainView;
@synthesize hideDropShadow;


#pragma mark - NSObject

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.backgroundColor = [UIColor colorWithRed:(CGFloat)220/255 green:(CGFloat)220/255  blue:(CGFloat)220/255  alpha:1];
        
        mainView = [[UIView alloc] initWithFrame:CGRectMake( 20.0f, 20.0f, self.frame.size.width-40, 200)];
      
      //  mainView.backgroundColor=[UIColor colorWithRed:5.0/255.0 green:32.0/255.0 blue:62.0/255.0 alpha:1.0];
        mainView.backgroundColor=[UIColor colorWithRed:(CGFloat)1/255 green:(CGFloat)164/255 blue:(CGFloat)223/255 alpha:1];
        [self addSubview:mainView];
        
        UILabel *commentLabel =[[UILabel alloc]init];
        commentLabel.frame= CGRectMake(SCREEN_WIDTH/2-90, 20, 200, 40);
        commentLabel.text=@"Write Your Comments Here";
        commentLabel.font=[UIFont systemFontOfSize:12];
        commentLabel.textColor=[UIColor whiteColor];
        [mainView addSubview:commentLabel];
        
        
//        UIButton * commentsBtn=[[UIButton alloc]initWithFrame:CGRectMake(mainView.frame.size.width/2-45,155,80,20)];
//               commentsBtn.titleLabel.textAlignment=NSTextAlignmentCenter;
//        [commentsBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
//        [commentsBtn setTitle:@"POST" forState:UIControlStateNormal];
//        commentsBtn.titleLabel.font=[UIFont systemFontOfSize:12];
//       // [ commentsBtn addTarget:self action:@selector(commentButton:) forControlEvents:UIControlEventTouchUpInside];
//        [mainView addSubview:commentsBtn];

    
        
        UIView *textFieldView = [[UIView alloc]init];
        textFieldView.frame=CGRectMake(0, 80, mainView.frame.size.width, 50);
        textFieldView.backgroundColor=[UIColor whiteColor];
        [mainView addSubview:textFieldView];
        
       
        
        UIImageView *messageIcon = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"IconAddComment.png"]];
        messageIcon.frame = CGRectMake( 9.0f, 17.0f, 19.0f, 17.0f);
        [mainView addSubview:messageIcon];
        
        UIImageView *commentBox = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"TextFieldComment.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(5.0f, 10.0f, 5.0f, 10.0f)]];
        commentBox.frame = CGRectMake(35.0f, 8.0f, self.frame.size.width+100, 35.0f);
        [mainView addSubview:commentBox];
        
        commentField = [[UITextField alloc] initWithFrame:CGRectMake( 20.0f, 80.0f, self.frame.size.width-80,50)];
        commentField.font = [UIFont systemFontOfSize:14.0f];
        commentField.placeholder = @"                  Enter your Text";
        commentField.returnKeyType = UIReturnKeySend;
    //    commentField.backgroundColor=[UIColor whiteColor];
        
        commentField.textColor = [UIColor colorWithRed:73.0f/255.0f green:55.0f/255.0f blue:35.0f/255.0f alpha:1.0f];
        commentField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
       [commentField setValue:[UIColor colorWithRed:154.0f/255.0f green:146.0f/255.0f blue:138.0f/255.0f alpha:1.0f] forKeyPath:@"_placeholderLabel.textColor"]; // Are we allowed to modify private properties like this? -Héctor
        [mainView addSubview:commentField];
        
        //
        
        
        UIView *borderline2 = [[UIView alloc]init];
        borderline2.frame=CGRectMake(2, 43, mainView.frame.size.width-4, 2);
        borderline2.backgroundColor=[UIColor blueColor];
        [textFieldView addSubview:borderline2];
        

        
        
        
                UIView *borderline = [[UIView alloc]init];
        borderline.frame=CGRectMake(2, 38, 2, 7);
        borderline.backgroundColor=[UIColor blueColor];
        [textFieldView addSubview:borderline];
        
        
        UIView *borderline1 = [[UIView alloc]init];
        borderline1.frame=CGRectMake(textFieldView.frame.size.width-4, 38, 2, 7);
        borderline1.backgroundColor=[UIColor blueColor];
        [textFieldView addSubview:borderline1];
        
        
       
        
        
        //
    }
    return self;
}


#pragma mark - UIView

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
    
    if (!hideDropShadow) {
       
    }
}


#pragma mark - PAPPhotoDetailsFooterView

+ (CGRect)rectForView {
    return CGRectMake( 0.0f, 0.0f, [UIScreen mainScreen].bounds.size.width,300);
}

@end
