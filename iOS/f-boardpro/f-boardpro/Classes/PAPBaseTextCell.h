//
//  PAPBaseTextCell.h
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/15/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

@class FBSDKProfilePictureView;
@protocol PAPBaseTextCellDelegate;
#import "PAPBaseTextCell.h"
#import <UIKit/UIKit.h>

@interface PAPBaseTextCell : UITableViewCell {
    NSUInteger horizontalTextSpace;
    id _delegate;
}

/*! 
 Unfortunately, objective-c does not allow you to redefine the type of a property,
 so we cannot set the type of the delegate here. Doing so would mean that the subclass
 of would not be able to define new delegate methods (which we do in PAPActivityCell).
 */
@property (nonatomic, strong) id delegate;

/*! The cell's views. These shouldn't be modified but need to be exposed for the subclass */
@property (nonatomic, strong) UIView *mainView;
@property (nonatomic, strong) UIButton *nameButton;
@property (nonatomic, strong) UIButton *avatarImageButton;
@property (nonatomic, strong) FBSDKProfilePictureView *avatarImageView;
@property (nonatomic, strong) UILabel *contentLabel;
@property (nonatomic, strong) UILabel *timeLabel;
@property (nonatomic, strong) UIImageView *separatorImage;

/*! The horizontal inset of the cell */
@property (nonatomic) CGFloat cellInsetWidth;

/*! Setters for the cell's content */
- (void)setContentText:(NSString *)contentString;
- (void)setDate:(NSDate *)date;

- (void)setCellInsetWidth:(CGFloat)insetWidth;
- (void)hideSeparator:(BOOL)hide;

/*! Static Helper methods */
+ (CGFloat)heightForCellWithName:(NSString *)name contentString:(NSString *)content;
+ (CGFloat)heightForCellWithName:(NSString *)name contentString:(NSString *)content cellInsetWidth:(CGFloat)cellInset;
+ (NSString *)padString:(NSString *)string withFont:(UIFont *)font toWidth:(CGFloat)width;

@end

/*! Layout constants */
#define vertBorderSpacing 8.0f
#define vertElemSpacing 0.0f

#define horiBorderSpacing 8.0f
#define horiBorderSpacingBottom 9.0f
#define horiElemSpacing 5.0f

#define vertTextBorderSpacing 10.0f

#define avatarX horiBorderSpacing
#define avatarY vertBorderSpacing
#define avatarDim 33.0f

#define nameX avatarX+avatarDim+horiElemSpacing
#define nameY vertTextBorderSpacing
#define nameMaxWidth 200.0f

#define timeX avatarX+avatarDim+horiElemSpacing

/*!
 The protocol defines methods a delegate of a PAPBaseTextCell should implement.
 */
@protocol PAPBaseTextCellDelegate <NSObject>
@optional

/*!
 Sent to the delegate when a user button is tapped
 @param aUser the PFUser of the user that was tapped
 */


@end

