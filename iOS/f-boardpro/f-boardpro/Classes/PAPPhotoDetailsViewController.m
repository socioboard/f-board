//
//  PAPPhotoDetailViewController.m
//  Anypic
//
//  Created by Mattieu Gamache-Asselin on 5/15/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import "PAPPhotoDetailsViewController.h"
#import "PAPBaseTextCell.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "SUCache.h"
#import "SingletonClass.h"
//#import "PAPActivityCell.h"
#import "PAPPhotoDetailsFooterView.h"


enum ActionSheetTags {
    MainActionSheetTag = 0,
    ConfirmDeleteActionSheetTag = 1
};

@interface PAPPhotoDetailsViewController ()
@property (nonatomic, strong) UITextField *commentTextField;
@property (nonatomic, strong) PAPPhotoDetailsHeaderView *headerView;
@property (nonatomic, assign) BOOL likersQueryInProgress;
@end

static const CGFloat kPAPCellInsetWidth = 20.0f;

@implementation PAPPhotoDetailsViewController

@synthesize commentTextField;


#pragma mark - Initialization

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    
}

- (id)initWithDict:(NSDictionary *)dict {
    self = [super initWithStyle:UITableViewStylePlain];
    if (self) {
        self.Groupdict=[[NSDictionary alloc] init];
        self.Groupdict=dict;
        NSLog(@"group dic %@",self.Groupdict);
      NSArray  *arr=[[self.Groupdict objectForKey:@"comments"] objectForKey:@"data"];
        arrofCommentUser=[NSMutableArray arrayWithArray:arr];
    }
    return self;
}


#pragma mark - UIViewController

- (void)viewDidLoad {
    [self.tableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];

    [super viewDidLoad];
    
    self.navigationItem.titleView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"logo.png"]];
    
    // Set table view properties
    UIView *texturedBackgroundView = [[UIView alloc] initWithFrame:self.view.bounds];
     texturedBackgroundView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
    self.tableView.backgroundView = texturedBackgroundView;
    
    // Set table header
//    self.headerView.delegate = self;
    
    self.headerView = [[PAPPhotoDetailsHeaderView alloc]initWithFrame:self.Groupdict];
    self.headerView.delegate = self;
    self.headerView.frame=self.headerView.rect;
//    self.headerView.frame= CGRectMake(0, 0,250, 250);
    self.tableView.tableHeaderView = self.headerView;

    
    // Set table footer
    PAPPhotoDetailsFooterView *footerView = [[PAPPhotoDetailsFooterView alloc] initWithFrame:[PAPPhotoDetailsFooterView rectForView]];
    commentTextField = footerView.commentField;
    commentTextField.delegate = self;
    self.tableView.tableFooterView = footerView;

    if (NSClassFromString(@"UIActivityViewController")) {
        // Use UIActivityViewController if it is available (iOS 6 +)
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAction target:self action:@selector(activityButtonAction:)];
    } else if ([self currentUserOwnsPhoto]) {
        // Else we only want to show an action button if the user owns the photo and has permission to delete it.
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAction target:self action:@selector(actionButtonAction:)];
    }
    
    // Register to be notified when the keyboard will be shown to scroll the view
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];

    [self.headerView reloadLikeBar];

  }

#pragma mark - UITableViewDelegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:[[arrofCommentUser objectAtIndex:indexPath.row] objectForKey:@"message"]];
        // Add Background Color for Smooth rendering
    [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
        // Add Main Font Color
    [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
        // Add paragraph style
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
    [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
        // Add Font
    [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
        // And finally set the text on the label to use this
        //    label.attributedText = attributedString;
    
        // Phew. Now let's make the Bounding Rect
    CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(tableView.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];

    return expectedLabelSize.size.height+25;
}

-(NSString *)dateDiff:(NSString *)origDate {
    
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setFormatterBehavior:NSDateFormatterBehavior10_4];
    [df setDateFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZZZZ"];
    NSDate *convertedDate = [df dateFromString:origDate];
    NSDate *todayDate = [NSDate date];
    double ti = [convertedDate timeIntervalSinceDate:todayDate];
    ti = ti * -1;
    if(ti < 1) {
        return @"never";
    } else 	if (ti < 60) {
        return @"less than a minute ago";
    } else if (ti < 3600) {
        int diff = round(ti / 60);
        return [NSString stringWithFormat:@"%d minutes ago", diff];
    } else if (ti < 86400) {
        int diff = round(ti / 60 / 60);
        return[NSString stringWithFormat:@"%d hours ago", diff];
    } else if (ti < 2629743) {
        int diff = round(ti / 60 / 60 / 24);
        return[NSString stringWithFormat:@"%d days ago", diff];
    } else {
        return @"never";
    }
}


#pragma mark - PFQueryTableViewController


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{

    return arrofCommentUser.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellID = @"CommentCell";

    // Try to dequeue a cell and create one if necessary
    PAPBaseTextCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
    if (cell == nil) {
        cell = [[PAPBaseTextCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellID];
        cell.cellInsetWidth = kPAPCellInsetWidth;
       cell.delegate = self;
    }

    NSLog(@"arr %@",arrofCommentUser[indexPath.row]);

    cell.contentLabel.text=[NSString stringWithFormat:@"%@",[[arrofCommentUser objectAtIndex:indexPath.row] objectForKey:@"message"] ];

//    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    
    
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:cell.contentLabel.text];
        // Add Background Color for Smooth rendering
    [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
        // Add Main Font Color
    [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
        // Add paragraph style
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
    [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
        // Add Font
    [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
        // And finally set the text on the label to use this
        //    label.attributedText = attributedString;
    
        // Phew. Now let's make the Bounding Rect
    CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(tableView.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];

    
        //adjust the label the the new height.
    CGRect newFrame = cell.contentLabel.frame;
    newFrame.size.height = expectedLabelSize.size.height;
    cell.contentLabel.frame = newFrame;
    cell.avatarImageView.profileID=[[[arrofCommentUser objectAtIndex:indexPath.row] objectForKey:@"from"] objectForKey:@"id"];
//    [cell.avatarImageButton setTitle:[[[arrofCommentUser objectAtIndex:indexPath.row] objectForKey:@"from"] objectForKey:@"name"] forState:UIControlStateNormal];

//      cell.cellInsetWidth = kPAPCellInsetWidth;
    cell.timeLabel.text=[self dateDiff:[[arrofCommentUser objectAtIndex:indexPath.row] objectForKey:@"created_time"]];
//    NSLog(@"contentLabel %@  timeLabel  %@",cell.contentLabel,cell.timeLabel);
        return cell;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{

    
}

#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    if (textField.text!=nil) {
        
        NSLog(@"%@",textField.text);
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
       NSString *str=[NSString stringWithFormat:@"/%@/comments",[self.Groupdict objectForKey:@"id"]];
    NSLog(@"group dict %@",self.Groupdict);
    
        NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:textField.text,@"message",nil];
        FBSDKGraphRequest *requestparam = [[FBSDKGraphRequest alloc] initWithGraphPath:str
                                                                       parameters:params
                                                                       HTTPMethod:@"POST"];
        
        [requestparam startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id resultValue, NSError *errors) {
            dispatch_async(dispatch_get_main_queue(), ^{
              [self.tableView beginUpdates];
                
//                [[[arrofCommentUser objectAtIndex:indexPath.row] objectForKey:@"from"] objectForKey:@"id"];
                NSDictionary *from=[NSDictionary dictionaryWithObjectsAndKeys:[FBSDKAccessToken currentAccessToken].userID,@"id", nil];
                NSDictionary *dict=[NSDictionary dictionaryWithObjectsAndKeys:textField.text,@"message", from,@"from",nil];
                [arrofCommentUser addObject:dict];
                [self.tableView insertRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:arrofCommentUser.count-1 inSection:0]]
                                             withRowAnimation:UITableViewRowAnimationRight];
                NSIndexPath *path=[NSIndexPath indexPathForRow:arrofCommentUser.count-1 inSection:0];
                textField.text=nil;
                [self.tableView endUpdates];
            });
        }];
    }
    return [textField resignFirstResponder];
    
 }


#pragma mark - UIActionSheetDelegate

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
   }


#pragma mark - UIScrollViewDelegate

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    [commentTextField resignFirstResponder];
}


#pragma mark - PAPBaseTextCellDelegate

- (void)cell:(PAPBaseTextCell *)cellView didTapUserButton:(NSString *)aUser {
    [self shouldPresentAccountViewForUser:aUser];
}


#pragma mark - PAPPhotoDetailsHeaderViewDelegate

-(CGFloat)heightForHeaderInView{
    return 40;
}


-(void)photoDetailsHeaderView:(PAPPhotoDetailsHeaderView *)headerView didTapUserButton:(UIButton *)button user:(NSString *)user {
    [self shouldPresentAccountViewForUser:user];
}

- (void)actionButtonAction:(id)sender {
    UIActionSheet *actionSheet = [[UIActionSheet alloc] init];
    actionSheet.delegate = self;
    actionSheet.tag = MainActionSheetTag;
    actionSheet.destructiveButtonIndex = [actionSheet addButtonWithTitle:NSLocalizedString(@"Delete Photo", nil)];
    if (NSClassFromString(@"UIActivityViewController")) {
        [actionSheet addButtonWithTitle:NSLocalizedString(@"Share Photo", nil)];
    }
    actionSheet.cancelButtonIndex = [actionSheet addButtonWithTitle:NSLocalizedString(@"Cancel", nil)];
    [actionSheet showFromTabBar:self.tabBarController.tabBar];
}

- (void)activityButtonAction:(id)sender {
    }


#pragma mark - ()

- (void)showShareSheet {
    
    }

- (void)handleCommentTimeout:(NSTimer *)aTimer {
    }

- (void)shouldPresentAccountViewForUser:(NSString *)user {

}

- (void)backButtonAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)userLikedOrUnlikedPhoto:(NSNotification *)note {
    [self.headerView reloadLikeBar];
}

- (void)keyboardWillShow:(NSNotification*)note {
    // Scroll the view to the comment text box
    NSDictionary* info = [note userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    [self.tableView setContentOffset:CGPointMake(0.0f, self.tableView.contentSize.height-kbSize.height) animated:YES];
}

- (void)loadLikers {
    }

- (BOOL)currentUserOwnsPhoto {
    return YES;
}

- (void)shouldDeletePhoto {
    // Delete all activites related to this photo
   
}

@end
