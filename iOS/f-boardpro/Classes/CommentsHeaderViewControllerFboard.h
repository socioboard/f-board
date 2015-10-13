//
//  PAPPhotoDetailsHeaderView.h
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/15/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//
#import <UIKit/UIKit.h>


@interface CommentsHeaderViewControllerFboard : UIView

@property (nonatomic, strong)  UITapGestureRecognizer *tapRecognizer;
@property(nonatomic) BOOL toggle ;
/*! @name Managing View Properties */

/// The photo displayed in the view

@property(nonatomic,strong)UIImageView *avatarImageView;
/// The user that took the photo
@property (copy, nonatomic) NSString *userID;
@property (copy, nonatomic) NSString *userName;
@property (readonly) CGFloat desiredHeight;

@property(nonatomic,strong)NSDictionary *groupDict;
/// Array of the users that liked the photo
@property (nonatomic, strong) NSArray *likeUsers;
@property(nonatomic,strong)UIImageView *groupView;
/// Heart-shaped like button
@property (nonatomic, strong, readonly) UIButton *likeButton;
@property(nonatomic,strong)UIScrollView *scrollView;
/*! @name Delegate */
@property (nonatomic, strong) id delegate;
@property (nonatomic)CGRect rect;

+ (CGRect)rectForView;

- (id)initWithFrame:(NSDictionary*)aPhoto;
- (id)initWithFrame:(CGRect)frame photo:(NSString*)aPhoto photographer:(NSString*)aPhotographer likeUsers:(NSArray*)theLikeUsers;

- (void)setLikeButtonState:(BOOL)selected;
- (void)reloadLikeBar;
@end

/*!
 The protocol defines methods a delegate of a PAPPhotoDetailsHeaderView should implement.
 */
@protocol PAPPhotoDetailsHeaderViewDelegate <NSObject>
-(CGFloat)heightForHeaderInView;
@optional

/*!
 Sent to the delegate when the photgrapher's name/avatar is tapped
 @param button the tapped UIButton
 @param user the PFUser for the photograper
 */

- (void)CommentsHeaderViewControllerFboard:(CommentsHeaderViewControllerFboard *)headerView didTapUserButton:(UIButton *)button user:(NSString *)user;

@end