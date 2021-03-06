//
//  PostViewController.h
//  TwitterBoard
//
//  Created by Sumit on 21/04/15.
//. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <sqlite3.h>

@interface PostViewControllerFboard : UIViewController<UITextFieldDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate>
{
    UIDatePicker *datepicker;
    NSString *databasePath;
    sqlite3 *contactDB;
UIImagePickerController *picker;
}
@property(nonatomic,strong) UIButton *postImgButton;
@property(nonatomic,strong) UIButton *textPostButton;
@property(nonatomic,strong) UIButton *videoPostButton;
@property(nonatomic,strong)UIImageView *postImg;
@property(nonatomic,strong) UILocalNotification * localNotification;
@property(nonatomic,assign)BOOL mediaCheck;
@property(nonatomic,strong)NSMutableArray *feedsArray;
@property(nonatomic,strong)NSMutableArray *msgArray;
@property(nonatomic,strong) NSMutableArray *descArray;
@property(nonatomic,strong) UILabel *_selectedDate;
@property(nonatomic,strong) UIButton *sharePostBtn;
@property(nonatomic,strong) UITextField *pageTextField;
@property(nonatomic,strong)UIImage *imageToPost;
@property(nonatomic,strong)NSString *stringToPost;
@end
