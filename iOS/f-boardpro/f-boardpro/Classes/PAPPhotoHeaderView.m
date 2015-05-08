//
//  PAPPhotoHeaderView.m
//  Anypic
//
//  Created by Héctor Ramos on 5/15/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import "PAPPhotoHeaderView.h"
#import "TTTTimeIntervalFormatter.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
//#import "PAPUtility.h"
#import "UIImageView+WebCache.h"
#import "PAPPhotoDetailsViewController.h"
@class FBSDKProfilePictureView;
@interface PAPPhotoHeaderView ()
{
    PAPPhotoDetailsViewController * pdVC;
}

@property (nonatomic, strong) UIView *containerView;

//@property (nonatomic, strong) UIButton *userButton;
//@property (nonatomic, strong) UILabel *timestampLabel,*PlaceLabel;
@property (nonatomic, strong) TTTTimeIntervalFormatter *timeIntervalFormatter;
@property (nonatomic, strong) NSString * place;
@end


@implementation PAPPhotoHeaderView
@synthesize containerView;
@synthesize avatarImageView;
@synthesize userButton;
@synthesize timestampLabel,PlaceLabel,place;
@synthesize timeIntervalFormatter;
//@synthesize photo;
@synthesize buttons;
@synthesize likeButton,tagButton;
@synthesize commentButton;
@synthesize delegate;

#pragma mark - Initialization

- (id)initWithFrame:(CGRect)frame buttons:(PAPPhotoHeaderButtons)otherButtons {
    self = [super initWithFrame:frame];
    if (self) {
        [PAPPhotoHeaderView validateButtons:otherButtons];
        buttons = otherButtons;

        self.clipsToBounds = NO;
        self.containerView.clipsToBounds = NO;
        self.superview.clipsToBounds = NO;
        [self setBackgroundColor:[UIColor clearColor]];
        
        // translucent portion
        self.containerView = [[UIView alloc] initWithFrame:CGRectMake( 20.0f, 0.0f, self.bounds.size.width - 20.0f * 2.0f, self.bounds.size.height+10)];
        [self addSubview:self.containerView];
        [self.containerView setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"footer.png"]]];
        
        
        self.avatarImageView = [[UIImageView alloc] init];
        self.avatarImageView.frame = CGRectMake( 4.0f, 4.0f, 35.0f, 35.0f);
//        [self.avatarImageView.profileButton addTarget:self action:@selector(didTapUserButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        [self.containerView addSubview:self.avatarImageView];
        
        
        if (self.buttons & PAPPhotoHeaderButtonsComment) {
            // comments button
            commentButton = [UIButton buttonWithType:UIButtonTypeCustom];
            [containerView addSubview:self.commentButton];
            [self.commentButton setFrame:CGRectMake( 242.0f, 10.0f, 29.0f, 28.0f)];
            [self.commentButton setBackgroundColor:[UIColor clearColor]];
            [self.commentButton setTitle:@"" forState:UIControlStateNormal];
            [self.commentButton setTitleColor:[UIColor colorWithRed:0.369f green:0.271f blue:0.176f alpha:1.0f] forState:UIControlStateNormal];
            [self.commentButton setTitleShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f] forState:UIControlStateNormal];
            [self.commentButton setTitleEdgeInsets:UIEdgeInsetsMake( -4.0f, 0.0f, 0.0f, 0.0f)];
            [[self.commentButton titleLabel] setShadowOffset:CGSizeMake( 0.0f, 1.0f)];
            [[self.commentButton titleLabel] setFont:[UIFont systemFontOfSize:12.0f]];
            [[self.commentButton titleLabel] setMinimumScaleFactor:0.8f];
            [[self.commentButton titleLabel] setAdjustsFontSizeToFitWidth:YES];
            [self.commentButton setBackgroundImage:[UIImage imageNamed:@"footer.png"] forState:UIControlStateNormal];
            [self.commentButton setSelected:NO];
        }
        
        tagButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [containerView addSubview:self.tagButton];
        [self.tagButton setFrame:CGRectMake(175.0f, 8.0f, 29.0f, 29.0f)];
        [self.tagButton setBackgroundColor:[UIColor clearColor]];
        [self.tagButton setTitle:@"" forState:UIControlStateNormal];
        [self.tagButton setTitleColor:[UIColor colorWithRed:0.369f green:0.271f blue:0.176f alpha:1.0f] forState:UIControlStateNormal];
        [self.tagButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
        [self.tagButton setTitleShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f] forState:UIControlStateNormal];
        [self.tagButton setTitleShadowColor:[UIColor colorWithWhite:0.0f alpha:0.750f] forState:UIControlStateSelected];
        [self.tagButton setTitleEdgeInsets:UIEdgeInsetsMake(0.0f, 0.0f, 0.0f, 0.0f)];
        [[self.tagButton titleLabel] setShadowOffset:CGSizeMake(0.0f, 1.0f)];
        [[self.tagButton titleLabel] setFont:[UIFont systemFontOfSize:12.0f]];
        [[self.tagButton titleLabel] setMinimumScaleFactor:0.8f];
        [[self.tagButton titleLabel] setAdjustsFontSizeToFitWidth:YES];
        [self.tagButton setAdjustsImageWhenHighlighted:NO];
        [self.tagButton setAdjustsImageWhenDisabled:NO];
        [self.tagButton setBackgroundImage:[UIImage imageNamed:@"tagpicSelected.png"] forState:UIControlStateNormal];
        [self.tagButton setBackgroundImage:[UIImage imageNamed:@"tagpicSelected.png"] forState:UIControlStateSelected];
        //[self.tagButton addTarget:self action:@selector(didTapTagButton) forControlEvents:UIControlEventTouchUpInside];
        [self.tagButton setSelected:NO];
        
        
        if (self.buttons & PAPPhotoHeaderButtonsLike) {
            // like button
            likeButton = [UIButton buttonWithType:UIButtonTypeCustom];
            [containerView addSubview:self.likeButton];
            [self.likeButton setFrame:CGRectMake(206.0f, 8.0f, 29.0f, 29.0f)];
            [self.likeButton setBackgroundColor:[UIColor clearColor]];
            [self.likeButton setTitle:@"" forState:UIControlStateNormal];
            [self.likeButton setTitleColor:[UIColor colorWithRed:0.369f green:0.271f blue:0.176f alpha:1.0f] forState:UIControlStateNormal];
            [self.likeButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
            [self.likeButton setTitleShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f] forState:UIControlStateNormal];
            [self.likeButton setTitleShadowColor:[UIColor colorWithWhite:0.0f alpha:0.750f] forState:UIControlStateSelected];
            [self.likeButton setTitleEdgeInsets:UIEdgeInsetsMake(0.0f, 0.0f, 0.0f, 0.0f)];
            [[self.likeButton titleLabel] setShadowOffset:CGSizeMake(0.0f, 1.0f)];
            [[self.likeButton titleLabel] setFont:[UIFont systemFontOfSize:12.0f]];
            [[self.likeButton titleLabel] setMinimumScaleFactor:0.8f];
            [[self.likeButton titleLabel] setAdjustsFontSizeToFitWidth:YES];
            [self.likeButton setAdjustsImageWhenHighlighted:NO];
            [self.likeButton setAdjustsImageWhenDisabled:NO];
            [self.likeButton setBackgroundImage:[UIImage imageNamed:@"heart.png"] forState:UIControlStateNormal];
            [self.likeButton setBackgroundImage:[UIImage imageNamed:@"heart.png"] forState:UIControlStateSelected];
            [self.likeButton setSelected:NO];
        }
        
        if (self.buttons & PAPPhotoHeaderButtonsUser) {
            // This is the user's display name, on a button so that we can tap on it
            userButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
            [self.userButton setFrame:CGRectMake(-25.0f, 8.0f, 229.0f, 29.0f)];
//            userButton.backgroundColor=[UIColor redColor];
            [containerView addSubview:userButton];
          [self.userButton setBackgroundColor:[UIColor clearColor]];
            [[self.userButton titleLabel] setFont:[UIFont boldSystemFontOfSize:13]];
            [[self.userButton titleLabel] setTextAlignment:NSTextAlignmentLeft];
            [self.userButton setTitleColor:[UIColor colorWithRed:73.0f/255.0f green:55.0f/255.0f blue:35.0f/255.0f alpha:1.0f] forState:UIControlStateNormal];
            [self.userButton setTitleColor:[UIColor colorWithRed:134.0f/255.0f green:100.0f/255.0f blue:65.0f/255.0f alpha:1.0f] forState:UIControlStateHighlighted];
            [[self.userButton titleLabel] setLineBreakMode:NSLineBreakByTruncatingTail];
            [[self.userButton titleLabel] setShadowOffset:CGSizeMake( 0.0f, 1.0f)];
            [self.userButton setTitleShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f] forState:UIControlStateNormal];
        }
        
        self.timeIntervalFormatter = [[TTTTimeIntervalFormatter alloc] init];
        
        // timestamp
        self.timestampLabel = [[UILabel alloc] initWithFrame:CGRectMake( 50.0f, 26.0f, containerView.bounds.size.width - 49.0f - 72.0f, 18.0f)];
        [containerView addSubview:self.timestampLabel];
        [self.timestampLabel setTextColor:[UIColor colorWithRed:124.0f/255.0f green:124.0f/255.0f blue:124.0f/255.0f alpha:1.0f]];
        [self.timestampLabel setShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f]];
        [self.timestampLabel setShadowOffset:CGSizeMake( 0.0f, 1.0f)];
        [self.timestampLabel setFont:[UIFont systemFontOfSize:11.0f]];
        [self.timestampLabel setBackgroundColor:[UIColor clearColor]];
        
//        CALayer *layer = [containerView layer];
//        layer.backgroundColor = [[UIColor whiteColor] CGColor];
//        layer.masksToBounds = NO;
//        layer.shadowRadius = 1.0f;
//        layer.shadowOffset = CGSizeMake( 0.0f, 2.0f);
//        layer.shadowOpacity = 0.5f;
//        layer.shouldRasterize = YES;
//        layer.shadowPath = [UIBezierPath bezierPathWithRect:CGRectMake( 0.0f, containerView.frame.size.height - 4.0f, containerView.frame.size.width, 4.0f)].CGPath;
    
    self.PlaceLabel=[[UILabel alloc]initWithFrame:CGRectMake( 50.0f, 37.0f, containerView.bounds.size.width - 50.0f - 72.0f, 18.0f)];
    //self.PlaceLabel.text=self.place;
    [self.containerView addSubview:self.PlaceLabel];
    [self.PlaceLabel setTextColor:[UIColor colorWithRed:124.0f/255.0f green:124.0f/255.0f blue:124.0f/255.0f alpha:1.0f]];
    [self.PlaceLabel setShadowColor:[UIColor colorWithWhite:1.0f alpha:0.750f]];
    [self.PlaceLabel setShadowOffset:CGSizeMake( 0.0f, 1.0f)];
    [self.PlaceLabel setFont:[UIFont systemFontOfSize:11.0f]];
    [self.PlaceLabel setBackgroundColor:[UIColor clearColor]];
    
    CALayer *layer1 = [containerView layer];
    layer1.backgroundColor = [[UIColor whiteColor] CGColor];
    layer1.masksToBounds = NO;
    layer1.shadowRadius = 1.0f;
    layer1.shadowOffset = CGSizeMake( 0.0f, 2.0f);
    layer1.shadowOpacity = 0.5f;
    layer1.shouldRasterize = YES;
    layer1.shadowPath = [UIBezierPath bezierPathWithRect:CGRectMake( 0.0f, containerView.frame.size.height - 4.0f, containerView.frame.size.width, 4.0f)].CGPath;
    }
    return self;
}


#pragma mark - PAPPhotoHeaderView

- (void)setPhoto:(NSDictionary *)aPhoto {


    NSString *authorName = [[aPhoto objectForKey:@"from"] objectForKey:@"name"];
    
    NSString *added = @"";
    for(int i =0 ;i<[authorName length]; i++) {
        
        if (i==20) {
            break ;
        }
        char character = [authorName characterAtIndex:i];
        if (i==19) {
            character = '.';
        }
        NSString *temp = [NSString stringWithFormat:@"%c",character];
        added = [added stringByAppendingString:temp];
        
    }
        //    NSLog(@"added is %@",added);
    
    [self.userButton setTitle:added forState:UIControlStateNormal];
    
    
//    [self.userButton setTitle:authorName forState:UIControlStateNormal];
    
    CGFloat constrainWidth = containerView.bounds.size.width;

    if (self.buttons & PAPPhotoHeaderButtonsUser) {
        [self.userButton addTarget:self action:@selector(didTapUserButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    
    if (self.buttons & PAPPhotoHeaderButtonsComment) {
        constrainWidth = self.commentButton.frame.origin.x;
        [self.commentButton addTarget:self action:@selector(didTapCommentOnPhotoButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    
    if (self.buttons & PAPPhotoHeaderButtonsLike) {
        constrainWidth = self.likeButton.frame.origin.x;
        [self.likeButton addTarget:self action:@selector(didTapLikePhotoButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    
    // we resize the button to fit the user's name to avoid having a huge touch area
    CGPoint userButtonPoint = CGPointMake(50.0f, 6.0f);
    constrainWidth -= userButtonPoint.x;
    CGSize constrainSize = CGSizeMake(constrainWidth, containerView.bounds.size.height - userButtonPoint.y*2.0f);

    CGSize userButtonSize = [self.userButton.titleLabel.text boundingRectWithSize:constrainSize
                                                    options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesLineFragmentOrigin
                                                 attributes:@{NSFontAttributeName:self.userButton.titleLabel.font}
                                                    context:nil].size;
    
   CGRect userButtonFrame = CGRectMake(userButtonPoint.x, userButtonPoint.y, userButtonSize.width, userButtonSize.height);
//     CGRect userButtonFrame = CGRectMake(-20, userButtonPoint.y, userButtonSize.width, userButtonSize.height);
    NSLog(@"userButtonFrame =%f",userButtonFrame.origin.x);
       [self.userButton setFrame:userButtonFrame];
    
//    NSTimeInterval timeInterval = [[self.photo createdAt] timeIntervalSinceNow];
//    NSString *timestamp = [self.timeIntervalFormatter stringForTimeInterval:timeInterval];
//    [self.timestampLabel setText:timestamp];

    [self setNeedsDisplay];
}

- (void)setLikeStatus:(BOOL)liked {
    [self.likeButton setSelected:liked];
    
    if (liked) {
        [self.likeButton setTitleEdgeInsets:UIEdgeInsetsMake(-1.0f, 0.0f, 0.0f, 0.0f)];
        [[self.likeButton titleLabel] setShadowOffset:CGSizeMake(0.0f, -1.0f)];
    } else {
        [self.likeButton setTitleEdgeInsets:UIEdgeInsetsMake(0.0f, 0.0f, 0.0f, 0.0f)];
        [[self.likeButton titleLabel] setShadowOffset:CGSizeMake(0.0f, 1.0f)];
    }
}

- (void)shouldEnableLikeButton:(BOOL)enable {
    if (enable) {
        [self.likeButton removeTarget:self action:@selector(didTapLikePhotoButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    } else {
        [self.likeButton addTarget:self action:@selector(didTapLikePhotoButtonAction:) forControlEvents:UIControlEventTouchUpInside];
    }
}

#pragma mark - ()

+ (void)validateButtons:(PAPPhotoHeaderButtons)buttons {
    if (buttons == PAPPhotoHeaderButtonsNone) {
        [NSException raise:NSInvalidArgumentException format:@"Buttons must be set before initializing PAPPhotoHeaderView."];
    }
}

- (void)didTapUserButtonAction:(UIButton *)sender {
    if (delegate && [delegate respondsToSelector:@selector(photoHeaderView:didTapUserButton:user:)]) {
//        [delegate photoHeaderView:self didTapUserButton:sender user:[self.photo objectForKey:kPAPPhotoUserKey]];
    }
}

- (void)didTapLikePhotoButtonAction:(UIButton *)button {
    if (delegate && [delegate respondsToSelector:@selector(photoHeaderView:didTapLikePhotoButton:photo:)]) {
//        [delegate photoHeaderView:self didTapLikePhotoButton:button photo:self.photo];
    }
}

- (void)didTapCommentOnPhotoButtonAction:(UIButton *)sender {
    if (delegate && [delegate respondsToSelector:@selector(photoHeaderView:didTapCommentOnPhotoButton:photo:)]) {
//        [delegate photoHeaderView:self didTapCommentOnPhotoButton:sender photo:self.photo];
    }
}
/*-(void)didTapTagButton:(UIButton *)sender{
    
    if (delegate && [delegate respondsToSelector:@selector(photoHeaderView:didTapCommentOnPhotoButton:photo:)]) {
        [delegate photoHeaderView:self didTapTagButton:sender photo:self.photo];
    }
}*/
    
    
    

@end
