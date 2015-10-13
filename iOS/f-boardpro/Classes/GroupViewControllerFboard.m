//
//  GroupViewControllerFboard.m
//  TwitterBoard
//
//  Created by GBS-ios on 4/23/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import "GroupViewControllerFboard.h"
#import "SingletonClass.h"
#import "SUCacheFboard.h"
#import "CommentsViewControllerFboard.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>
#import "TableCustomCellFboard.h"
#import "DisplayGroupViewControllerFboard.h"

@interface GroupViewControllerFboard ()
{
    UILabel *messageLabel;
}

@end

@implementation GroupViewControllerFboard

- (void)viewDidLoad {
    [super viewDidLoad];
    self.admistratorArray = [[NSMutableArray alloc]init];
    self.unreadArray = [[NSMutableArray alloc]init];
    self.grpIdArray = [[NSMutableArray alloc]init];
    self.grpNameArray = [[NSMutableArray alloc]init];
   [self createFollowTable];
    [self fetchGroupFeeds];
    self.view.backgroundColor = [UIColor whiteColor];
    
    UIImageView *imageView = [[UIImageView alloc]init];
    imageView.frame=CGRectMake(70,40, SCREEN_WIDTH-140, 30);
    
    
    UIImage *image = [UIImage imageNamed:@"fbrandpro.png"];
    [imageView setImage:image];
    [self.view addSubview:imageView];
    
    
    messageLabel =[[UILabel alloc]init];
    messageLabel.frame=CGRectMake(20,80, SCREEN_WIDTH-40,250);
    messageLabel.backgroundColor=[UIColor whiteColor];
    messageLabel.numberOfLines=0;
    messageLabel.lineBreakMode=NSLineBreakByWordWrapping;
    messageLabel.textAlignment=NSTextAlignmentCenter;
    if ([UIScreen mainScreen].bounds.size.width==320)
    {
        
        messageLabel.text=@"In Accordance With recent API Guidelines,\nFacebook has stopped allowing regular users \nto manage group via API\n\nOnly Testers,developers and App admins are authorised so,to become a tester,please send a friend request to the following FB id\n\n\nwith message\n\n\n\n\nFor more updates join SociBoard group on FB";
    }
    else
    {
        messageLabel.text=@"In Accordance With recent API Guidelines,\nFacebook has stopped allowing regular users \nto manage group via API\n\nOnly Testers,developers and App admins are authorised so,to become a tester,please send a friend request to the following FB id\n\n\nwith message\n\n\n\n\n\n\n\n\n\n\n\nFor more updates join SociBoard group on FB";
    }
    
    messageLabel.font=[UIFont boldSystemFontOfSize:12];
    [self.view addSubview:messageLabel];
    
    
    UILabel * label1 = [[UILabel alloc]init];
    
    label1.frame=CGRectMake(20, 300, SCREEN_WIDTH-40, 30);
    label1.backgroundColor=[UIColor clearColor];
    label1.numberOfLines=1;
    label1.textAlignment=NSTextAlignmentCenter;
    label1.font=[UIFont italicSystemFontOfSize:12];
    label1.text = @"\"please add me to Fboardpro app\"";
    [self.view addSubview:label1];
    
    
    UILabel *linkLabel = [[UILabel alloc]init];
    linkLabel.frame=CGRectMake(20, 240, SCREEN_WIDTH-40, 20);
    linkLabel.textColor=ThemeColorFboard;
    linkLabel.numberOfLines=1;
    linkLabel.textAlignment=NSTextAlignmentCenter;
    linkLabel.font=[UIFont italicSystemFontOfSize:12];
    linkLabel.userInteractionEnabled=YES;
    NSDictionary *underlineAttribute = @{NSUnderlineStyleAttributeName: @(NSUnderlineStyleSingle)};
    linkLabel.attributedText = [[NSAttributedString alloc] initWithString:@"https://www.facebook.com/sumit ghosh"
                                                               attributes:underlineAttribute];
    UITapGestureRecognizer * tapOnLinkLbl=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(openSumitSirFbProfile)];
    [linkLabel addGestureRecognizer:tapOnLinkLbl];
    [self.view addSubview:linkLabel];
    
    
    UILabel *linkLabelFbGroup = [[UILabel alloc]init];
    linkLabelFbGroup.frame=CGRectMake(20, 350, SCREEN_WIDTH-40, 20);
    linkLabelFbGroup.textColor=ThemeColorFboard;
    linkLabelFbGroup.numberOfLines=2;
    linkLabelFbGroup.textAlignment=NSTextAlignmentCenter;
    linkLabelFbGroup.font=[UIFont italicSystemFontOfSize:12];
    linkLabelFbGroup.userInteractionEnabled=YES;
    underlineAttribute = @{NSUnderlineStyleAttributeName: @(NSUnderlineStyleSingle)};
    linkLabelFbGroup.attributedText = [[NSAttributedString alloc] initWithString:@"https://www.facebook.com/groups"
                                                                      attributes:underlineAttribute];
    UITapGestureRecognizer * tapOnLinkLblFbGroup=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(openFbGroup)];
    [linkLabelFbGroup addGestureRecognizer:tapOnLinkLblFbGroup];
    [self.view addSubview:linkLabelFbGroup];
}


-(void)openSumitSirFbProfile
{
    NSURL * urlTemp= [NSURL URLWithString:@"https://www.facebook.com/sumit.ghosh?ref=ts&fref=ts"];
    [[UIApplication sharedApplication]openURL:urlTemp];
}
-(void)openFbGroup
{
    NSURL * urlTemp= [NSURL URLWithString:@"https://www.facebook.com/groups/621124567991195/"];
    [[UIApplication sharedApplication]openURL:urlTemp];
    
}






    
    
    
 
    
    
    
    // Do any additional setup after loading the view.


-(void)viewWillAppear:(BOOL)animated{

    self.navigationController.navigationBarHidden=YES;
}
-(void)createFollowTable
{
    groupTableView=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width,self.view.frame.size.height) style:UITableViewStylePlain];
    groupTableView.dataSource=self;
    groupTableView.delegate=self;
    groupTableView.backgroundColor=[UIColor whiteColor];
   // [self.view addSubview:groupTableView];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)fetchGroupFeeds{
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }

    
    NSIndexPath *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {

    [FBSDKAccessToken setCurrentAccessToken:token];
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/groups" parameters:nil];

    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {

        
        for (NSDictionary *dic in [result objectForKey:@"data"] ) {
            NSString *str1 = [dic objectForKey:@"administrator"];
            NSString *str2 = [dic objectForKey:@"id"];
            NSString *str3 = [dic objectForKey:@"name"];
            NSString *str4 = [dic objectForKey:@"unread"];
            if (str1) {
                [self.admistratorArray addObject:str1];
            }else{
                [self.admistratorArray addObject:@"nil"];
            }
            
            if (str2) {
                [self.grpIdArray addObject:str2];
            }else{
                [self.grpIdArray addObject:@"nil"];
            }
            
            
            if (str3) {
                [self.grpNameArray addObject:str3];
            }else{
                [self.grpNameArray addObject:@"nil"];
            }
            
            if (str4) {
                [self.unreadArray addObject:str4];
            }else{
                [self.unreadArray addObject:@"nil"];
            }
            
            
        }
         //[self createFollowTable];
//        NSLog(@"adminstrator array is %@",self.admistratorArray);
//          NSLog(@"grpId array is %@",self.grpIdArray);
//          NSLog(@"grp name array is %@",self.grpNameArray);
//          NSLog(@"unreadArray array is %@",self.unreadArray);

    }];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Follow";
    
   TableCustomCellFboard *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCellFboard alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    cell.layer.cornerRadius=5;
    [cell.layer setBorderColor: [[UIColor blackColor] CGColor]];
    [cell.layer setBorderWidth:1];

    
    
    
    NSString *fromIdImage = [self.grpIdArray objectAtIndex:indexPath.row];
    
    if (![fromIdImage isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.userImage setImageWithURL:[NSURL URLWithString:fromIdImage] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }

    
    NSString *str1 =[self.grpNameArray objectAtIndex:indexPath.row];
    
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        cell.userNameDesc.text=@"nothing";
    }
    cell.likeCountLabel.text = @"";
    cell.commentCountLabel.text = @"";
   
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSString *accessToken1=[[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
    NSString *str1 =[self.grpIdArray objectAtIndex:indexPath.row];
    NSIndexPath *indexPaths=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPaths.section+indexPaths.row].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",str1] parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
            
            NSString *str11 = [result objectForKey:@"description"];
            NSString *str4 = [[result objectForKey:@"from"] objectForKey:@"name"];
            NSString *str3= [result objectForKey:@"icon"];
            NSString *str2 = [[result objectForKey:@"owner"]objectForKey:@"name"];
            NSString *str5 = [result objectForKey:@"id"];

            
           
            
           
            
            DisplayGroupViewControllerFboard *disp = [[DisplayGroupViewControllerFboard alloc]init];
            disp.email=str2;
            disp.ownerName = str4;
            disp.desc= str11;
            disp.iconUrl = str3;
            disp.idStr = str5;

            self.navigationController.navigationBarHidden=NO;
            [self.navigationController pushViewController:disp animated:YES];

            
        }];
    
 
    //
    NSString * urlStr=[NSString stringWithFormat:@"https://graph.facebook.com/%@/?access_token=%@",str1,token];
   
    NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSURL *url45=[NSURL URLWithString:urlStr2];
    
    NSMutableURLRequest * requestdf=[[NSMutableURLRequest alloc]initWithURL:url45 cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:requestdf returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    NSLog(@"reponse is %@",response);
  

    
        NSString *str11 = [response objectForKey:@"description"];
        NSString *str2 = [response objectForKey:@"email"];
        NSString *str3= [response objectForKey:@"icon"];
        NSString *str4 = [[response objectForKey:@"owner"]objectForKey:@"name"];
    NSString *str5 = [response objectForKey:@"id"];
    
//    DisplayGroupViewControllerFboard *disp = [[DisplayGroupViewControllerFboard alloc]init];
//    disp.email=str2;
//    disp.ownerName = str4;
//    disp.desc= str11;
//    disp.iconUrl = str3;
//    disp.idStr = str5;
//   
//    [self presentViewController:disp animated:YES completion:nil]; //
    }
}


-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return 1;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.grpNameArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 100;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
