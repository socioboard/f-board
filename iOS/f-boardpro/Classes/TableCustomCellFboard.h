//
//  TableCustomCell.h
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIImageView+WebCache.h"
@interface TableCustomCellFboard : UITableViewCell
{
    UIToolbar *toolBar;
    

}
@property(nonatomic,strong)UIImageView * userImage;
@property(nonatomic,strong)UIImageView *fromUserImage;
@property(nonatomic,strong)UILabel *fromLabel;
@property(nonatomic,strong)UIButton * add_minusButton;;
@property(nonatomic,strong)UILabel * userNameDesc;
@property(nonatomic,strong)UILabel *likeCountLabel;
@property(nonatomic,strong)UILabel *commentCountLabel;
@property(nonatomic,strong)UILabel *userNameLabel;
@property(nonatomic,strong)UILabel *timeLabel;
//-------------------
@property(nonatomic,strong)UILabel * headingTop;
@property(nonatomic,strong)UILabel *likeLabel;
@property(nonatomic,strong)UILabel *descriptionHeadingLbl;
@property(nonatomic,strong)UILabel *placeLabel;
@property(nonatomic,strong)UILabel *peopleWhereLabel;

@property(nonatomic,strong)UIView * containerView;




@end
