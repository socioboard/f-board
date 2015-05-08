//
//  PAPPhotoDetailsHeaderView.m
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/15/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import "PAPPhotoDetailsHeaderView.h"
#import "PAPProfileImageView.h"
#import "PAPBaseTextCell.h"
#import "UIImageView+WebCache.h"
#import "SUCache.h"
#import "SingletonClass.h"
#import "TTTTimeIntervalFormatter.h"
#import "PAPPhotoDetailsViewController.h"

#define baseHorizontalOffset 20.0f
#define baseWidth 280.0f


//#define horiBorderSpacing 6.0f
#define horiMediumSpacing 8.0f

//#define vertBorderSpacing 6.0f
#define vertSmallSpacing 2.0f


#define nameHeaderX baseHorizontalOffset
#define nameHeaderY 0.0f
#define nameHeaderWidth baseWidth
#define nameHeaderHeight 46.0f

#define avatarImageX horiBorderSpacing
#define avatarImageY vertBorderSpacing
#define avatarImageDim 35.0f

#define nameLabelX avatarImageX+avatarImageDim+horiMediumSpacing
#define nameLabelY avatarImageY+vertSmallSpacing
#define nameLabelMaxWidth 280.0f - (horiBorderSpacing+avatarImageDim+horiMediumSpacing+horiBorderSpacing)

#define timeLabelX nameLabelX
#define timeLabelMaxWidth nameLabelMaxWidth

#define mainImageX baseHorizontalOffset
#define mainImageY nameHeaderHeight
#define mainImageWidth baseWidth
#define mainImageHeight 280.0f

#define likeBarX baseHorizontalOffset
#define likeBarY nameHeaderHeight + mainImageHeight
#define likeBarWidth baseWidth
#define likeBarHeight 43.0f

#define likeButtonX 9.0f
#define likeButtonY 7.0f
#define likeButtonDim 28.0f

#define likeProfileXBase 46.0f
#define likeProfileXSpace 3.0f
#define likeProfileY 6.0f
#define likeProfileDim 30.0f

#define viewTotalHeight likeBarY+likeBarHeight
#define numLikePics 7.0f

@interface PAPPhotoDetailsHeaderView ()
{
    UIImage *image1;
}

// View components
@property (nonatomic, strong) UIView *nameHeaderView;
@property (nonatomic, strong) UILabel *descriptionLabel;
@property (nonatomic, strong) UIImageView *photoImageView;
@property (nonatomic, strong) UIView *likeBarView;
@property (nonatomic, strong) NSMutableArray *currentLikeAvatars;

// Redeclare for edit

// Private methods
- (CGRect)createView;

@end


NSDateFormatter *timeFormatter;

@implementation PAPPhotoDetailsHeaderView


@synthesize likeUsers;
@synthesize nameHeaderView;
@synthesize descriptionLabel;
@synthesize photoImageView;
@synthesize likeBarView;
@synthesize likeButton;
@synthesize delegate;
@synthesize currentLikeAvatars;

#pragma mark - NSObject

- (id)initWithFrame:(NSDictionary*)aPhoto{
    CGRect rectView;
    rectView  =[self createFrame];
    self.rect=rectView;
 self = [super initWithFrame:rectView];
    self.rect=rectView;
        // Initialization code
        if (!timeFormatter) {
            timeFormatter = [[NSDateFormatter alloc] init];
        }
        self.likeUsers=[[NSArray alloc] init];
        
        self.groupDict=[[NSDictionary alloc] init];
        self.groupDict=aPhoto;
    NSArray *arr=[[self.groupDict objectForKey:@"likes"] objectForKey:@"data"];
        
        self.likeUsers =arr;

        
        NSLog(@"group dict %@",self.groupDict);
        self.backgroundColor = [UIColor clearColor];
    
//    self.rect=rectView;

   
    [self createView];
    self.backgroundColor=[UIColor clearColor];
    return self;
}

-(void) handleTapGesture:(UITapGestureRecognizer*)tapGesture{
    NSLog(@"in tap gesture recogniser");
    
    
}



- (id)initWithFrame:(CGRect)frame photo:(NSString*)aPhoto photographer:(NSString*)aPhotographer likeUsers:(NSArray*)theLikeUsers {
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        if (!timeFormatter) {
            timeFormatter = [[NSDateFormatter alloc] init];
        }

        self.photo = aPhoto;
               self.likeUsers = theLikeUsers;
        
        self.backgroundColor = [UIColor clearColor];

    
    }
    return self;
}

#pragma mark - UIView

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
   
}


#pragma mark - PAPPhotoDetailsHeaderView

+ (CGRect)rectForView {
//    return CGRectMake( 0.0f, 0.0f, [UIScreen mainScreen].bounds.size.width, viewTotalHeight);
    return CGRectMake( 0.0f, 0.0f, [UIScreen mainScreen].bounds.size.width, likeBarHeight);

}

- (void)setPhoto:(NSString *)aPhoto {
 

}

- (void)setLikeUsers:(NSMutableArray *)anArray {
    
    
    NSString *idValue= [self.groupDict objectForKey:@"id"];
    
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
        //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
    
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"%@/likes",idValue] parameters:nil];
 
    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
        if (self.scrollView) {
            self.scrollView==nil;
        }
        NSArray *arr34=[response objectForKey:@"data"];
        [self.likeButton setTitle:[NSString stringWithFormat:@"%lu",(unsigned long)arr34.count] forState:UIControlStateNormal];
        
        self.scrollView=[[UIScrollView alloc]init];
        self.scrollView.frame=CGRectMake(20, 0, likeBarView.frame.size.width-30,likeBarView.frame.size.height);
        self.scrollView.backgroundColor=[UIColor clearColor];
        [self.likeBarView addSubview:self.scrollView];
        self.scrollView.contentSize=CGSizeMake(arr34.count*40, self.scrollView.frame.size.height);
        
        for (int i = 0; i < arr34.count; i++) {
            
            FBSDKProfilePictureView *profilePic = [[FBSDKProfilePictureView alloc]init];
            [profilePic setFrame:CGRectMake(likeProfileXBase + i * (likeProfileXSpace + likeProfileDim), likeProfileY, likeProfileDim, likeProfileDim)];
            profilePic.tag = i;
            profilePic.profileID=[[arr34 objectAtIndex:i] objectForKey:@"id"];
            NSLog(@"%@",profilePic.profileID);
            [self.scrollView addSubview:profilePic];
            [currentLikeAvatars addObject:profilePic];
        }
    }];
//    [self setNeedsDisplay];

}

- (void)setLikeButtonState:(BOOL)selected {
    if (selected) {
        [likeButton setTitleEdgeInsets:UIEdgeInsetsMake( -1.0f, 0.0f, 0.0f, 0.0f)];
        [[likeButton titleLabel] setShadowOffset:CGSizeMake( 0.0f, -1.0f)];
    } else {
        [likeButton setTitleEdgeInsets:UIEdgeInsetsMake( 0.0f, 0.0f, 0.0f, 0.0f)];
        [[likeButton titleLabel] setShadowOffset:CGSizeMake( 0.0f, 1.0f)];
    }
    [likeButton setSelected:selected];
}

- (void)reloadLikeBar {
    
   }


#pragma mark - ()

-(CGRect)createFrame{
    NSString *str=[self.groupDict objectForKey:@"message"];
    if (!str) {
       str =[self.groupDict objectForKey:@"description"];
    }
      NSString *str2=[self.groupDict objectForKey:@"picture"];
   
    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    
    CGSize expectedLabelSize = [str sizeWithFont:[UIFont fontWithName:@"ariel" size:.1] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
//  return   CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width,likeBarHeight+mainImageY+mainImageHeight+expectedLabelSize.height+nameHeaderY+50 );
    
    if (str2!=nil) {
        return   CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width,likeBarHeight+mainImageY+mainImageHeight+expectedLabelSize.height+nameHeaderY+50 );

    }else{
    return   CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width,likeBarHeight+expectedLabelSize.height+nameHeaderY+50 );
    
    }
}

-(CGRect )createView {
    /*
     Create middle section of the header view; the image
     */
    
 
    self.descriptionLabel=[[UILabel alloc] initWithFrame:CGRectMake(nameHeaderX-2, nameHeaderY+47,self.frame.size.width-35,100)];
     self.descriptionLabel.font=[UIFont fontWithName:@"ariel" size:5];
    self.descriptionLabel.textColor=[UIColor blackColor];
    self.descriptionLabel.backgroundColor=[UIColor clearColor];
    self.descriptionLabel.text=[self.groupDict objectForKey:@"message"];
    if (self.descriptionLabel.text==nil) {
        self.descriptionLabel.text=[self.groupDict objectForKey:@"description"];
    }
    if (self.descriptionLabel.text==nil) {
          self.descriptionLabel.text=[self.groupDict objectForKey:@"story"];
    }
    NSLog(@"%@",self.descriptionLabel.text);
        // self.descriptionLabel.font=[UIFont fontWithName:@"ariel" size:5.0f];
    self.descriptionLabel.lineBreakMode=NSLineBreakByWordWrapping;
    self.descriptionLabel.backgroundColor=[UIColor whiteColor];
    self.descriptionLabel.numberOfLines=0;
    [self addSubview:self.descriptionLabel];
    
   CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    NSMutableAttributedString *attributedString;
    
    if (self.descriptionLabel.text) {
         attributedString = [[NSMutableAttributedString alloc] initWithString:self.descriptionLabel.text];
   }
   
        // Add Background Color for Smooth rendering
    [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
        // Add Main Font Color
    [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
        // Add paragraph style
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
    [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
        // Add Font
    [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
        // And finally set the text on the label to use this
        //    label.attributedText = attributedString;
    
        // Phew. Now let's make the Bounding Rect
    CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(self.descriptionLabel.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];

    
    
    
//    CGSize expectedLabelSize = [self.descriptionLabel.text sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
    
    
        //adjust the label the the new height.
    CGRect newFrame = self.descriptionLabel.frame;
    newFrame.size.height = expectedLabelSize.size.height;
     newFrame.size.width = self.descriptionLabel.frame.size.width;
    self.descriptionLabel.frame = newFrame;

    
    [self.descriptionLabel sizeToFit];
   
    NSString *str=[self.groupDict objectForKey:@"picture"];
    self.groupView = [[UIImageView alloc] initWithFrame:CGRectMake(mainImageX, self.descriptionLabel.frame.size.height+mainImageY+4, self.frame.size.width-35, mainImageHeight)];
    
   [self.groupView setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@""]];
    self.groupView.backgroundColor = [UIColor blackColor];
    self.groupView.contentMode = UIViewContentModeScaleAspectFit;
    
    if (str!=nil) {
         [self addSubview:self.groupView];
    }
   
    
    self.nameHeaderView = [[UIView alloc] initWithFrame:CGRectMake(nameHeaderX, nameHeaderY, self.frame.size.width-35, nameHeaderHeight)];
    self.nameHeaderView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"BackgroundComments.png"]];
    self.nameHeaderView.backgroundColor=[UIColor whiteColor];
    [self addSubview:self.nameHeaderView];
    
  

  
        //===========================
    
    NSString *str3=[[self.groupDict objectForKey:@"from"] objectForKey:@"id"];
    NSString *profilestr=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",str3]; ;

    NSLog(@"Urls Str %@",profilestr);
    
    self.avatarImageView = [[UIImageView alloc] initWithFrame:CGRectMake(avatarImageX+10, avatarImageY, avatarImageDim, avatarImageDim)];
    
    [self.avatarImageView setImageWithURL:[NSURL URLWithString:profilestr] placeholderImage:[UIImage imageNamed:@"heart.png"]];
    self.avatarImageView.backgroundColor = [UIColor blackColor];
    self.groupView.contentMode = UIViewContentModeScaleAspectFit;
    [self addSubview:self.avatarImageView];

    
    
  
        // Create name label
    NSString *nameString = [[self.groupDict objectForKey:@"from"] objectForKey:@"name"];
    UIButton *userButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [nameHeaderView addSubview:userButton];
    [userButton setBackgroundColor:[UIColor clearColor]];
    [[userButton titleLabel] setFont:[UIFont boldSystemFontOfSize:15.0f]];
    [userButton setTitle:nameString forState:UIControlStateNormal];
    [userButton setTitleColor:[UIColor colorWithRed:73.0f/255.0f green:55.0f/255.0f blue:35.0f/255.0f alpha:1.0f] forState:UIControlStateNormal];
    [userButton setTitleColor:[UIColor colorWithRed:134.0f/255.0f green:100.0f/255.0f blue:65.0f/255.0f alpha:1.0f] forState:UIControlStateHighlighted];
    [[userButton titleLabel] setLineBreakMode:NSLineBreakByTruncatingTail];
    [[userButton titleLabel] setShadowOffset:CGSizeMake(0.0f, 1.0f)];
    [userButton setTitleShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f] forState:UIControlStateNormal];
    [userButton addTarget:self action:@selector(didTapUserNameButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    
        // we resize the button to fit the user's name to avoid having a huge touch area
    CGPoint userButtonPoint = CGPointMake(50.0f, 6.0f);
    CGFloat constrainWidth = self.nameHeaderView.bounds.size.width - (self.avatarImageView.bounds.origin.x +self.avatarImageView.bounds.size.width);
    CGSize constrainSize = CGSizeMake(constrainWidth, self.nameHeaderView.bounds.size.height - userButtonPoint.y*2.0f);
    CGSize userButtonSize = [userButton.titleLabel.text boundingRectWithSize:constrainSize
                                                                     options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesLineFragmentOrigin
                                                                  attributes:@{NSFontAttributeName:userButton.titleLabel.font}
                                                                     context:nil].size;
    
    
    CGRect userButtonFrame = CGRectMake(userButtonPoint.x, userButtonPoint.y, userButtonSize.width, userButtonSize.height);
    [userButton setFrame:userButtonFrame];
    
        // Create time label
   NSString *timeString = [self.groupDict objectForKey:@"created_time"];
   timeString= [timeString stringByReplacingOccurrencesOfString:@"T" withString:@" "];
    

    CGSize timeLabelSize = [timeString boundingRectWithSize:CGSizeMake(nameLabelMaxWidth, CGFLOAT_MAX)
                                                    options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesLineFragmentOrigin
                                                 attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.0f]}
                                                    context:nil].size;
    
    UILabel *timeLabel = [[UILabel alloc] initWithFrame:CGRectMake(timeLabelX, nameLabelY+userButtonSize.height, timeLabelSize.width, timeLabelSize.height)];
    [timeLabel setText:[self dateDiff:timeString]];
    [timeLabel setFont:[UIFont systemFontOfSize:11.0f]];
    [timeLabel setTextColor:[UIColor colorWithRed:124.0f/255.0f green:124.0f/255.0f blue:124.0f/255.0f alpha:1.0f]];
    [timeLabel setShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f]];
    [timeLabel setShadowOffset:CGSizeMake(0.0f, 1.0f)];
    [timeLabel setBackgroundColor:[UIColor clearColor]];
    [self.nameHeaderView addSubview:timeLabel];
    
    [self setNeedsDisplay];
    
        //===========================
    
//    likeBarView = [[UIView alloc] initWithFrame:CGRectMake(likeBarX, self.groupView.frame.origin.y+self.groupView.frame.size.height, self.frame.size.width-35, likeBarHeight)];
    if (str!=nil) {
    likeBarView = [[UIView alloc] initWithFrame:CGRectMake(likeBarX, self.groupView.frame.origin.y+self.groupView.frame.size.height, self.frame.size.width-35, likeBarHeight)];
    }else{
     likeBarView = [[UIView alloc] initWithFrame:CGRectMake(likeBarX,nameHeaderY+nameHeaderHeight+self.descriptionLabel.frame.size.height, self.frame.size.width-35, likeBarHeight)];
    
    }
    

    [likeBarView setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"footer.png"]]];
   [self addSubview:likeBarView];
//    
    
    
    likeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [likeButton setFrame:CGRectMake(likeButtonX, likeButtonY, likeButtonDim, likeButtonDim)];
    [likeButton setBackgroundColor:[UIColor clearColor]];
    [likeButton setTitleColor:[UIColor colorWithRed:0.369f green:0.271f blue:0.176f alpha:1.0f] forState:UIControlStateNormal];
    [likeButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
//    [likeButton setTitleShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f] forState:UIControlStateNormal];
//    [likeButton setTitleShadowColor:[UIColor colorWithWhite:0.0f alpha:0.750f] forState:UIControlStateSelected];
//    [likeButton setTitleEdgeInsets:UIEdgeInsetsMake(0.0f, 0.0f, 0.0f, 0.0f)];
    [[likeButton titleLabel] setFont:[UIFont systemFontOfSize:12.0f]];
    [[likeButton titleLabel] setMinimumScaleFactor:0.8f];
    [[likeButton titleLabel] setAdjustsFontSizeToFitWidth:YES];
    [[likeButton titleLabel] setShadowOffset:CGSizeMake(0.0f, 1.0f)];
    [likeButton setAdjustsImageWhenDisabled:NO];
    [likeButton setAdjustsImageWhenHighlighted:NO];
    [likeButton setBackgroundImage:[UIImage imageNamed:@"heart.png"] forState:UIControlStateNormal];
    [likeButton setBackgroundImage:[UIImage imageNamed:@"heart.png"] forState:UIControlStateSelected];
    [likeButton addTarget:self action:@selector(didTapLikePhotoButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    NSMutableArray *arrwd=[NSMutableArray arrayWithArray:[[self.groupDict objectForKey:@"likes"] objectForKey:@"data"]];
   
    [likeButton setTitle:[NSString stringWithFormat:@"%lu",(unsigned long)arrwd.count] forState:UIControlStateNormal];
    likeButton.userInteractionEnabled=YES;
    [likeBarView addSubview:likeButton];
     [self setLikeUsers:arrwd];
//   [self reloadLikeBar];
   
    UIImageView *separator = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"SeparatorComments.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0.0f, 1.0f, 0.0f, 1.0f)]];
    [separator setFrame:CGRectMake(0.0f, likeBarView.frame.size.height - 2.0f, likeBarView.frame.size.width, 2.0f)];
    
    [likeBarView addSubview:separator];
    
    
    self.tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapGesture:)];
   self.tapRecognizer.numberOfTapsRequired = 1;
    self.rect=CGRectMake(0, 0, likeBarWidth, self.likeBarView.frame.origin.y+self.likeBarView.frame.size.height);

    self.backgroundColor=[UIColor grayColor];
    return CGRectMake(0, 0,250, 250);
}

- (void)didTapLikePhotoButtonAction:(UIButton *)button {

    NSString *idValue= [self.groupDict objectForKey:@"id"];
    
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
        //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
    

        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"%@/likes",idValue] parameters:nil HTTPMethod:@"POST"];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
            NSString *str=[response valueForKey:@"success"];
            NSLog(@"%@",str);
            
            [self setLikeUsers:self.likeUsers];
            
        }];
    
        //code for dislikes
    
    FBSDKGraphRequest *request1 = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"%@/likes",idValue] parameters:nil HTTPMethod:@"DELETE"];
    
    [request1 startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
        
        if (error)
        {
            NSLog(@"error: %@", error.localizedDescription);
        }
        else
        {
            NSLog(@"ok!! %@",response);
        }
    }];

}


- (void)didTapLikerButtonAction:(UIButton *)button {
    
    
  }

- (void)didTapUserNameButtonAction:(UIButton *)button {
    
    
    
}

-(NSString *)dateDiff:(NSString *)origDate {
    
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setFormatterBehavior:NSDateFormatterBehavior10_4];
    [df setDateFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZZZZ"];
    NSDate *convertedDate = [df dateFromString:origDate];
    NSDate *todayDate = [NSDate date];
    double ti = [convertedDate timeIntervalSinceDate:todayDate];
    ti = ti * -1;
    if(ti < 1) {
        return @"never";
    } else 	if (ti < 60) {
        return @"less than a minute ago";
    } else if (ti < 3600) {
        int diff = round(ti / 60);
        return [NSString stringWithFormat:@"%d minutes ago", diff];
    } else if (ti < 86400) {
        int diff = round(ti / 60 / 60);
        return[NSString stringWithFormat:@"%d hours ago", diff];
    } else if (ti < 2629743) {
        int diff = round(ti / 60 / 60 / 24);
        return[NSString stringWithFormat:@"%d days ago", diff];
    } else {
        return @"never";
    }
}


@end
