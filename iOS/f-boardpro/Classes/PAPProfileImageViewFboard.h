//
//  PAPProfileImageView.h
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/17/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//


#import <FBSDKCoreKit/FBSDKCoreKit.h>
@interface PAPProfileImageViewFboard : UIView

@property (nonatomic, strong) UIButton *profileButton;
@property (nonatomic, strong) UIImageView *profileImageView;

//- (void)setFile:(PFFile *)file;
- (void)setFile:(NSString *)userId;
@end
