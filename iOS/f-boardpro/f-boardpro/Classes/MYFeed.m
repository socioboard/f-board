//
//  FollwViewController.m
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import "MYFeed.h"
#import "SUCache.h"
#import "AppDelegate.h"
#import "MBProgressHUD.h"
#import "TableCustomCell.h"
#import "SingletonClass.h"
#import "PAPPhotoDetailsViewController.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"

@interface MYFeed ()

@end

@implementation MYFeed
@synthesize feedsArray,msgArray;
- (void)viewDidLoad
{
    [super viewDidLoad];
    
//    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchFeeds:) name:@"CurrentUserChangedNotification" object:nil];

    // Do any additional setup after loading the view.
    
   }

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    self.navigationController.navigationBarHidden=YES;

     [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchFeeds:) name:@"CurrentUserChangedNotification" object:nil];
  [self fetchFeeds:@"nil"];
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];

}

-(void)changeUser:(NSNotificationCenter *)defaultCenter{


}


-(void)createFollowTable
{
    
    if (!followTableView) {
        followTableView=[[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width,self.view.frame.size.height) style:UITableViewStylePlain];
        followTableView.dataSource=self;
        followTableView.delegate=self;
        followTableView.backgroundColor=[UIColor whiteColor];
        [self.view addSubview:followTableView];
    }else{
        [followTableView reloadData];
    }
}
-(void)fetchFeeds:(NSString *)accessToken{
    
    [[AppDelegate sharedAppDelegate]showHUDLoadingView:@""];
    self.feedsArray = [[NSMutableArray alloc]init];
    self.msgArray = [[NSMutableArray alloc]init];
    self.descArray=[[NSMutableArray alloc]init];
    self.ownerIdArray=[[NSMutableArray alloc] init];
    self.grpIdArray=[[NSMutableArray alloc] init];
    self.dicArray=[[NSMutableArray alloc] init];
    self.feedDictArray=[[NSMutableArray alloc] init];
    self.ownerNameArray=[[NSMutableArray alloc] init];
    
    NSIndexPath  *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        NSLog(@"%@",[FBSDKAccessToken currentAccessToken].tokenString);
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/feed" parameters:nil];
   
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            
            [[AppDelegate sharedAppDelegate]hideHUDLoadingView];
            self.pagingString = [[result objectForKey:@"paging"]objectForKey:@"next"];
            NSString *pageStr = [[result objectForKey:@"paging"]objectForKey:@"previous"];
            if (self.pagingString) {
                    //NSLog(@"self.pagestr is %@",self.pagingString);
            }
            
              dispatch_async(dispatch_get_main_queue(), ^{
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSLog(@"dic %@",dic);
                
                NSString *str = [dic objectForKey:@"picture"];
                NSString *str1 = [dic objectForKey:@"message"];
                NSString *str2 = [dic objectForKey:@"description"];
                NSString *idValue=[dic objectForKey:@"id"];
                NSString *groupOwnerId=[[dic objectForKey:@"from"] objectForKey:@"id"];
                
                 NSString *groupOwnerName=[[dic objectForKey:@"from"] objectForKey:@"name"];
                NSString *timeStr=[dic objectForKey:@"created_time"];
                if (str==nil &&  str1==nil && str2==nil) {
                    
                }else if (str && str1 && str2){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];
                }
                else if( str&&str1){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:@"nil"];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];

                }else if(str1&&str2){
                    [self.feedsArray addObject:@"nil"];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.feedDictArray addObject:dic];
                     [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.grpIdArray addObject:idValue];

                }else if(str&&str2){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:@"nil"];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.feedDictArray addObject:dic];
                     [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.grpIdArray addObject:idValue];

                }else if(str){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:@"nil"];
                    [self.descArray addObject:@"nil"];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.dicArray addObject:timeStr];
                     [self.ownerNameArray addObject:groupOwnerName];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];

                }else if(str1){
                    [self.feedsArray addObject:@"nil"];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:@"nil"];
                     [self.ownerNameArray addObject:groupOwnerName];
                    [self.ownerIdArray addObject:groupOwnerId];
                     [self.dicArray addObject:timeStr];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];

                }else if(str2){
                    [self.feedsArray addObject:@"nil"];
                    [self.msgArray addObject:@"nil"];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.dicArray addObject:timeStr];
                     [self.ownerNameArray addObject:groupOwnerName];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];

                }
            }
              [self createFollowTable];
              
              });
            
          
            
            
            
        }];
    }
}


-(void)fetchHomeFeeds:(NSString *)accessToken{
    
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSString *accessTokn = [[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
    
    NSLog(@"access Token %@",accessTokn);
    NSString *userId = [[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    userId=@"1501093480148539";
    
   
    NSString * urlStr=[NSString stringWithFormat:@"/%@/feed?access_token=%@",userId,accessTokn];
    
    urlStr=[urlStr stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    FBSDKGraphRequest *requester = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/feed" parameters:nil];
    
    [requester startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *errort) {
        
    }];
    NSURL * url=[NSURL URLWithString:urlStr];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    
    NSMutableDictionary * innerJson = [NSJSONSerialization
                                       JSONObjectWithData:data options:kNilOptions error:&error
                                       ];
    
    NSLog(@"response is=========================== %@",response);
    
    
    
}

-(void)getLikesCount{
    
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    NSString *accessTokn = [[NSUserDefaults standardUserDefaults]objectForKey:@"LaccessToken"];
    
    NSLog(@"access Token %@",accessTokn);
    NSString *userId = [[NSUserDefaults standardUserDefaults]objectForKey:@"userId"];
    userId=@"1501093480148539";
    
    
    NSString * urlStr=[NSString stringWithFormat:@"/%@/feed?access_token=%@",userId,accessTokn];
    
    urlStr=[urlStr stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
   // FBRequest *friendRequest = [FBRequest requestForGraphPath:@"me/?fields=likes"];
    
    FBRequest *like = [FBRequest requestForGraphPath:@"1461321887470777?fields=likes"];
    
    [like startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error5) {
        
      //   NSLog(@"result is is    %@",result);
        
    }];
    
    NSURL * url=[NSURL URLWithString:urlStr];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    
    
    NSLog(@"response is=========================== %@",response);

}




- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Myfeed";
    
    TableCustomCell *cell = (TableCustomCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    
        if (cell == nil)
    {
        cell = [[TableCustomCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
       // cell.likeCountLabel.text=@"10 Like";
     
        
    }
    cell.timeLabel.text=@"";
//    cell.backgroundColor= [UIColor redColor];
    NSString *ownerId=[self.ownerIdArray objectAtIndex:indexPath.section];
    NSString *str =[self.feedsArray objectAtIndex:indexPath.section];
    NSString *timeStr=[self.dicArray objectAtIndex:indexPath.section];
    
    timeStr=[self dateDiff:timeStr];
    NSString *strurl=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",ownerId];
    NSLog(@"Owner strUrl  %@",strurl);
    cell.userNameLabel.text=[self.ownerNameArray objectAtIndex:indexPath.section];
    cell.fromUserImage.layer.cornerRadius=cell.fromUserImage.frame.size.width/2;
    [cell.fromUserImage setImageWithURL:[NSURL URLWithString:strurl]];
    cell.fromUserImage.clipsToBounds=YES;
    cell.timeLabel.text=timeStr;
    if (![str isEqualToString:@"nil"]) {
         [cell.userImage setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        cell.userImage.hidden=YES;
    }
 
    NSString *str1 =[self.msgArray objectAtIndex:indexPath.section];
    NSString *str2= [self.descArray objectAtIndex:indexPath.section];
       if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
           }else{
               if (![str2 isEqualToString:@"nil"]) {
                      cell.userNameDesc.text=str2;
               }else{
                   cell.userNameDesc.text=[[self.feedDictArray objectAtIndex:indexPath.section] objectForKey:@"story"];
//                   cell.userNameDesc.hidden=YES;
               }
     
    }
    
    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    
    
    
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:cell.userNameDesc.text];
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

    
//    CGSize expectedLabelSize = [cell.userNameDesc.text sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
    
        //adjust the label the the new height.
    CGRect newFrame = cell.userNameDesc.frame;
    newFrame.size.height = expectedLabelSize.size.height;
    newFrame.size.width =  cell.frame.size.width-20;
    cell.userNameDesc.frame = newFrame;
    [cell.userNameDesc sizeToFit];
       CGRect rect=cell.userImage.frame;
    cell.userImage.frame=CGRectMake(rect.origin.x,cell.userNameDesc.frame.origin.y+cell.userNameDesc.frame.size.height+5, rect.size.width, rect.size.height);
    
   
    
     return cell;
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
    }  else if (ti > 31535999) {
        int diff = round(ti / 31535999);
        return[NSString stringWithFormat:@"%d Years ago", diff];
    }  else if (ti > 2628000) {
        int diff = round(ti / 2628000);
        return[NSString stringWithFormat:@"%d months ago", diff];
    }
    else {
        return @"never";
    }
}


-(void)commentButton:(UIButton *)button{

}

-(void)likeButton:(UIButton *)button{

}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
    
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",[self.grpIdArray objectAtIndex:indexPath.section]] parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
            

            PAPPhotoDetailsViewController *photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
            self.navigationController.navigationBarHidden=NO;
            [self.navigationController pushViewController:photoDetail animated:YES];
            
            
        }];
        
    }

    
}
-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return self.feedsArray.count;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 35;
}

-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *view=[[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width,35)];
    view.backgroundColor=[UIColor blackColor];
    NSArray *likeArr=[[[self.feedDictArray objectAtIndex:section] objectForKey:@"likes"] objectForKey:@"data"];
    NSArray *commentArr=[[[self.feedDictArray objectAtIndex:section] objectForKey:@"comments"] objectForKey:@"data"];
    NSString *likeTitle=[NSString stringWithFormat:@"Likes %lu",(unsigned long)likeArr.count];
    
    NSString *commentTitle=[NSString stringWithFormat:@"Comments %lu",(unsigned long)commentArr.count];
    
    toolBar = [[UIToolbar alloc] initWithFrame:CGRectMake(0,0,tableView.frame.size.width , 30)];
    [toolBar setBarStyle:UIBarStyleDefault];
    
         NSMutableArray *barItems = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *likeButton = [[UIBarButtonItem alloc] initWithTitle:likeTitle  style:UIBarButtonItemStyleBordered target:self action:@selector(likeButton:)];
    likeButton.tag=section;
//    CGSize newSize = CGSizeMake(20.0f, 20.0f);
    [barItems addObject:likeButton];
    
    
    UIBarButtonItem *flexibleSpace = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:@selector(commentButton:)];
    [flexibleSpace setTitle:@"Comments"];
    
    [barItems addObject:flexibleSpace];
    
    UIBarButtonItem *commentButton = [[UIBarButtonItem alloc] initWithTitle:commentTitle  style:UIBarButtonItemStyleBordered target:self action:@selector(commentButton:)];
    commentButton.tag=section;
    [barItems addObject:commentButton];
    
    [toolBar setItems:barItems animated:YES];
    [view addSubview:toolBar];
    
    return view;

}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
     CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    NSString *str1 =[self.msgArray objectAtIndex:indexPath.section];
    NSString *str2= [self.descArray objectAtIndex:indexPath.section];
     NSString *str =[self.feedsArray objectAtIndex:indexPath.section];
     NSString *str3   = [[self.feedDictArray objectAtIndex:indexPath.section] objectForKey:@"story"];

    NSString *msgStr;
    if (![str1 isEqualToString:@"nil"]) {
        
//         expectedLabelSize = [str1 sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
        msgStr=str1;
    }else{
    if (![str2 isEqualToString:@"nil"]) {
//        expectedLabelSize = [str2 sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
        msgStr=str2;
    }else{
        msgStr=str3;
//    expectedLabelSize = [str3 sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
    
    }
    }
    
    
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:msgStr];
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
    CGRect boundingRect = [attributedString boundingRectWithSize:CGSizeMake(tableView.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
    
    
    if (![str isEqualToString:@"nil"]) {
        return 60+boundingRect.size.height+110+65;
    }else{
      return 60+boundingRect.size.height+65;
    }
    
   
}

-(void) tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
//    NSArray *likeArr=[[[self.feedDictArray objectAtIndex:indexPath.section] objectForKey:@"likes"] objectForKey:@"data"];
//    NSArray *commentArr=[[[self.feedDictArray objectAtIndex:indexPath.section] objectForKey:@"comments"] objectForKey:@"data"];
//    NSString *likeTitle=[NSString stringWithFormat:@"Likes %lu",(unsigned long)likeArr.count];
//    
//    NSString *commentTitle=[NSString stringWithFormat:@"Comments %lu",(unsigned long)commentArr.count];
    
    NSLog(@"index is %ld",(long)indexPath.section);
    NSLog(@"self.feeds array count is %lu",(unsigned long)self.feedsArray.count);
    
    if (indexPath.section == self.feedsArray.count-1) {
       [self retrieveMoreObjects];
    }
    if (cell!=nil) {
       
    }
    
}

-(void) retrieveMoreObjects{
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    
    NSString *accessToken1=[[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
    
    NSIndexPath  *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {
        [FBSDKAccessToken setCurrentAccessToken:token];

//    FBSDKAccessToken *token = [SUCache itemForSlot:slot].token;
    
    NSString *str1 =self.pagingString;
    
    NSString * urlStr=str1;
    
    NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSURL *url=[NSURL URLWithString:urlStr2];
    
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    
    
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    
    
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    NSLog(@"reponse is %@",response);
    
    self.pagingString = [[response objectForKey:@"paging"]objectForKey:@"next"];
    NSString *pageStr = [[response objectForKey:@"paging"]objectForKey:@"previous"];
    if (self.pagingString) {
            // NSLog(@"self.pagestr is %@",self.pagingString);
    }
    
            dispatch_async(dispatch_get_main_queue(), ^{
            for (NSDictionary *dic in [response objectForKey:@"data"] ) {
                NSLog(@"dic %@",dic);
                
                NSString *str = [dic objectForKey:@"picture"];
                NSString *str1 = [dic objectForKey:@"message"];
                NSString *str2 = [dic objectForKey:@"description"];
                NSString *idValue=[dic objectForKey:@"id"];
                NSString *groupOwnerId=[[dic objectForKey:@"from"] objectForKey:@"id"];
                
                NSString *groupOwnerName=[[dic objectForKey:@"from"] objectForKey:@"name"];
                NSString *timeStr=[dic objectForKey:@"created_time"];
                if (str==nil &&  str1==nil && str2==nil) {
                    
                }else if (str && str1 && str2){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];
                }
                else if( str&&str1){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:@"nil"];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];
                    
                }else if(str1&&str2){
                    [self.feedsArray addObject:@"nil"];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.feedDictArray addObject:dic];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.grpIdArray addObject:idValue];
                    
                }else if(str&&str2){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:@"nil"];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.feedDictArray addObject:dic];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.dicArray addObject:timeStr];
                    [self.grpIdArray addObject:idValue];
                    
                }else if(str){
                    [self.feedsArray addObject:str];
                    [self.msgArray addObject:@"nil"];
                    [self.descArray addObject:@"nil"];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.dicArray addObject:timeStr];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];
                    
                }else if(str1){
                    [self.feedsArray addObject:@"nil"];
                    [self.msgArray addObject:str1];
                    [self.descArray addObject:@"nil"];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.dicArray addObject:timeStr];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];
                    
                }else if(str2){
                    [self.feedsArray addObject:@"nil"];
                    [self.msgArray addObject:@"nil"];
                    [self.descArray addObject:str2];
                    [self.ownerIdArray addObject:groupOwnerId];
                    [self.dicArray addObject:timeStr];
                    [self.ownerNameArray addObject:groupOwnerName];
                    [self.feedDictArray addObject:dic];
                    [self.grpIdArray addObject:idValue];
                    
                }
            }
            
            });
    NSLog(@"self.feeds array is %@",self.feedsArray);
        
    }
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
