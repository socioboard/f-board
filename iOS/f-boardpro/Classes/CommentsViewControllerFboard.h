//
//  PAPPhotoDetailViewController.h
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/15/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import "CommentsHeaderViewControllerFboard.h"
#import "PAPBaseTextCellFboard.h"


@interface CommentsViewControllerFboard : UITableViewController <UITextFieldDelegate, UIActionSheetDelegate, PAPPhotoDetailsHeaderViewDelegate>{
    NSMutableArray *arrofCommentUser;
}
@property (nonatomic, strong) NSArray  *array;
@property(nonatomic,strong)NSDictionary *Groupdict;
//@property(nonatomic,strong)UIView *headerView;
- (id)initWithDict:(NSDictionary *)dict;
@property(nonatomic)CGFloat *height;


@end
