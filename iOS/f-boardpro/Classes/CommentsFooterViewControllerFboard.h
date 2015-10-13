//
//  PAPPhotoDetailsFooterView.h
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/16/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommentsFooterViewControllerFboard : UIView

@property (nonatomic, strong) UITextField *commentField;
@property (nonatomic) BOOL hideDropShadow;

+ (CGRect)rectForView;

@end
