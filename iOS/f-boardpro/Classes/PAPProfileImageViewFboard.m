//
//  PAPProfileImageView.m
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/17/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import "PAPProfileImageViewFboard.h"
#import "UIImageView+WebCache.h"
@class FBSDKProfilePictureView;

@interface PAPProfileImageViewFboard ()
@property (nonatomic, strong) UIImageView *borderImageview;
@end

@implementation PAPProfileImageViewFboard

@synthesize borderImageview;
@synthesize profileImageView;
@synthesize profileButton;


#pragma mark - NSObject

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        
        self.profileImageView = [[UIImageView alloc] initWithFrame:frame];
        [self addSubview:self.profileImageView];
        
        self.profileButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [self addSubview:self.profileButton];
        
        if (frame.size.width < 35.0f) {
            self.borderImageview = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"ShadowProfilePicture-29.png"]];
        } else if (frame.size.width < 43.0f) {
            self.borderImageview = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"ShadowProfilePicture-35.png"]];
        } else {
            self.borderImageview = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"ShadowProfilePicture-43.png"]];
        }
        
        [self addSubview:self.borderImageview];
    }
    return self;
}


#pragma mark - UIView

- (void)layoutSubviews {
    [super layoutSubviews];
    [self bringSubviewToFront:self.borderImageview];
    self.profileImageView.frame = CGRectMake( 1.0f, 0.0f, self.frame.size.width - 2.0f, self.frame.size.height - 2.0f);
    self.borderImageview.frame = CGRectMake( 0.0f, 0.0f, self.frame.size.width, self.frame.size.height);
    self.profileButton.frame = CGRectMake( 0.0f, 0.0f, self.frame.size.width, self.frame.size.height);
}

#pragma mark - PAPProfileImageView

- (void)setFile:(NSString *)userId {
    if (!userId) {
        return;
    }
    [self.profileImageView setImageWithURL:[NSURL URLWithString:userId] placeholderImage:[UIImage imageNamed:@""]];
//    self.profileImageView.profileID =userId;
    
}

@end
