//
//  InviteFriendController.h
//  f-boardpro
//

//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>

@interface InviteFriendController : UIViewController<UICollectionViewDataSource,UICollectionViewDelegate,FBSDKAppInviteDialogDelegate>
@property (nonatomic, strong) UICollectionView *listCollectionView;
@property(nonatomic,strong) UIButton *checkButton ;
@property(nonatomic,strong) NSMutableString *selectedFrndsString;
@end
