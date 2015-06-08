//
//  PostScheduleController.h
//  SwitchUserSample

//
//

#import <UIKit/UIKit.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import <sqlite3.h>

@interface PostScheduleController : UIViewController<UITextViewDelegate,UIScrollViewDelegate,UINavigationControllerDelegate,UIImagePickerControllerDelegate>{
    UIToolbar *toolBar;
      UIToolbar *pickerToolBar;
 UIDatePicker *datepicker;
    sqlite3 *_databaseHandle;
    UIView *pickerView;
    NSDate *dateToFire;
}
@property(nonatomic,strong)UIImage *selectedImage;
@property(nonatomic,strong)NSString *type;
@property(nonatomic,strong)NSString *classType;
@property(nonatomic,strong) UITextView *pageTextField;
@property(nonatomic,strong)UIScrollView *scrollView;
@property(nonatomic,strong)UIImageView *selectedImageView;
@property(nonatomic,strong) UILocalNotification * localNotification;
@property(nonatomic,strong) FBSDKProfilePictureView *profileImageView;
@property(nonatomic,strong) NSString *timeStamp;
@property(nonatomic,strong)UIImage *mediaPickImage;
@property(nonatomic,assign)BOOL mediaCheck;
@end
